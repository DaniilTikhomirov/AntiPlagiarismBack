package com.plagiarism.AntiPlagiarismBack.services;

import com.github.javaparser.ast.CompilationUnit;
import com.plagiarism.AntiPlagiarismBack.exeptions.InvalidLinkException;
import com.plagiarism.AntiPlagiarismBack.exeptions.RequestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.github.javaparser.JavaParser;

@Service
public class GitHubProjectsParser {

    private final HttpClient httpClient;

    @Value("${github.developer.token}")
    private String token;

    public GitHubProjectsParser() {
        this.httpClient = HttpClient.newHttpClient();
    }

    private String sendHttpRequest(String url, String token) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", "token " + token)
                .header("Accept", "application/vnd.github.v3+json")
                .build();

        System.out.println(url);

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RequestException("HTTP Error: " + response.statusCode() + " - " + response.body());
        }
        return response.body();
    }

    private String getFileContent(String owner, String repo, String filePath, String token, String branch) throws IOException, InterruptedException {
        String url = String.format("https://api.github.com/repos/%s/%s/contents/%s?ref=%s", owner, repo, filePath, branch);
        String response = sendHttpRequest(url, token);

        JSONObject jsonResponse = new JSONObject(response);
        String base64Content = jsonResponse.getString("content").replaceAll("\\s+", "");

        byte[] decodedBytes = Base64.getDecoder().decode(base64Content);
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }


    public String getRepoTree(String owner, String repo, String branch) throws IOException, InterruptedException {
        String url = String.format("https://api.github.com/repos/%s/%s/git/trees/%s?recursive=1", owner, repo, branch);
        String response = sendHttpRequest(url, token);

        JSONObject jsonResponse = new JSONObject(response);
        JSONArray tree = jsonResponse.getJSONArray("tree");

        StringBuilder methodsBuilder = new StringBuilder();
        for (int i = 0; i < tree.length(); i++) {
            JSONObject file = tree.getJSONObject(i);
            String filePath = file.getString("path");
            String type = file.getString("type");

            if ("blob".equals(type)) {
                if (filePath.endsWith(".java")) {
                    String fileContent = getFileContent(owner, repo, filePath, token, branch);
                    List<String> methods = parseJavaFile(fileContent);
                    methods.forEach(methodsBuilder::append);
                }else if(filePath.endsWith(".py") || filePath.endsWith(".html") ||
                        filePath.endsWith(".js") || filePath.endsWith(".jsx")) {
                    String fileContent = getFileContent(owner, repo, filePath, token, branch);
                    methodsBuilder.append(fileContent);
                }else {
                    methodsBuilder.append(filePath).append("\n");
                }
            }
        }
        return methodsBuilder.toString()
                .replaceAll("[\\s=?]", "");
    }

    private String getSourceBranchFromPullRequest(String owner, String repo, int pullRequestNumber, String token) throws IOException, InterruptedException {
        // Формируем URL для получения информации о пул-реквесте
        String url = String.format("https://api.github.com/repos/%s/%s/pulls/%d", owner, repo, pullRequestNumber);

        // Выполняем запрос к API GitHub
        String response = sendHttpRequest(url, token);

        // Парсим JSON-ответ, чтобы получить название исходной ветки
        JSONObject jsonResponse = new JSONObject(response);
        return jsonResponse.getJSONObject("head").getString("ref");
    }

    public String getFilesFromPullRequest(String owner, String repo, int pullNumber) throws IOException, InterruptedException {
        String url = String.format("https://api.github.com/repos/%s/%s/pulls/%d/files", owner, repo, pullNumber);
        String response = sendHttpRequest(url, token);
        String branch = getSourceBranchFromPullRequest(owner, repo, pullNumber, token);

        JSONArray filesArray = new JSONArray(response);
        StringBuilder methodsBuilder = new StringBuilder();

        for (int i = 0; i < filesArray.length(); i++) {
            JSONObject file = filesArray.getJSONObject(i);
            String filePath = file.getString("filename");

            if (filePath.endsWith(".java")) {
                String fileContent = getFileContent(owner, repo, filePath, token, branch);
                List<String> methods = parseJavaFile(fileContent);
                methods.forEach(methodsBuilder::append);
            } else {
                methodsBuilder.append(filePath).append("\n");
            }
        }

        return methodsBuilder.toString()
                .replaceAll("[\\s=?]", "");
    }

    private List<String> parseJavaFile(String content) {
        List<String> methods = new ArrayList<>();
        try {
            CompilationUnit cu = new JavaParser().parse(content).getResult().orElseThrow();

            cu.getTypes().forEach(type ->
                    type.getMethods().forEach(method ->
                            method.getBody().ifPresentOrElse(
                                    body -> methods.add(body.toString()),
                                    () -> methods.add("") // Если тела метода нет
                            )
                    )
            );
        } catch (Exception e) {
            System.err.println("Error parsing file: " + e.getMessage());
        }
        return methods;
    }

    private String getDefaultBranch(String owner, String repo, String token) throws IOException, InterruptedException {
        String url = String.format("https://api.github.com/repos/%s/%s", owner, repo);
        String response = sendHttpRequest(url, token);

        // Парсим JSON-ответ
        JSONObject jsonResponse = new JSONObject(response);
        return jsonResponse.getString("default_branch");
    }


    public String getRepoTreeByLink(String link) throws IOException, InterruptedException {
        if (link == null || link.isBlank()) {
            throw new InvalidLinkException("Invalid URL: The provided link is null or empty");
        }

        String[] splitLink = link.trim().split("/");
        String owner, repo;
        String defaultBranch;

        if (splitLink.length >= 7 && "tree".equals(splitLink[5])) {
            // Обработка ссылки на ветку
            owner = splitLink[3];
            repo = splitLink[4];
            StringBuilder branch = new StringBuilder(splitLink[6]);

            if(splitLink.length > 7){
                for (int i = 7; i < splitLink.length; i++){
                    branch.append("/").append(splitLink[i]);
                }
            }
            return getRepoTree(owner, repo, branch.toString());
        } else if (splitLink.length >= 6 && "pull".equals(splitLink[5])) {
            // Обработка ссылки на пул-реквест
            owner = splitLink[3];
            repo = splitLink[4];
            int pullNumber = Integer.parseInt(splitLink[6]);
            return getFilesFromPullRequest(owner, repo, pullNumber);
        } else if (splitLink.length >= 5) {
            // Обработка ссылки на основной репозиторий
            owner = splitLink[3];
            repo = splitLink[4];

            defaultBranch = getDefaultBranch(owner, repo, token);
            return getRepoTree(owner, repo, defaultBranch);
        }

        throw new InvalidLinkException("Invalid URL: " + link);
    }
}
