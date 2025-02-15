package com.plagiarism.AntiPlagiarismBack.kafka;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plagiarism.AntiPlagiarismBack.dto.HomeWorkContent;
import com.plagiarism.AntiPlagiarismBack.services.HomeWorkService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumer {
    private final HomeWorkService homeWorkService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "sheets-parser", groupId = "sheets")
    public void listen(String contents){

        try {
            List<HomeWorkContent> homeWorkContent = objectMapper.readValue(
                    contents, new TypeReference<>() {
                    }
            );
            homeWorkService.createHomeWork(homeWorkContent);
        } catch (Exception e) {
            log.error("Ошибка десериализации: {}", e.getMessage(), e);
        }
    }
}
