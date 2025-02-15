package com.plagiarism.AntiPlagiarismBack.services;

import com.plagiarism.AntiPlagiarismBack.dto.AddCheckWithStudent;
import com.plagiarism.AntiPlagiarismBack.dto.HomeWorkContent;
import com.plagiarism.AntiPlagiarismBack.dto.StudentDto;
import com.plagiarism.AntiPlagiarismBack.dto.UserContent;
import com.plagiarism.AntiPlagiarismBack.enums.HomeWorkType;
import com.plagiarism.AntiPlagiarismBack.mappers.CheckMapper;
import com.plagiarism.AntiPlagiarismBack.models.CheckSimilarity;
import com.plagiarism.AntiPlagiarismBack.models.HomeWork;
import com.plagiarism.AntiPlagiarismBack.models.Student;
import com.plagiarism.AntiPlagiarismBack.repository.HomeWorkRepository;
import com.plagiarism.AntiPlagiarismBack.repository.StudentRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class HomeWorkService {
    private final HomeWorkRepository homeWorkRepository;
    private final CalculateService calculateService;
    private final CheckService checkService;
    private final StudentService studentService;
    private final CheckMapper checkMapper;

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public List<HomeWork> createHomeWork(List<HomeWorkContent>homeWorksContent) throws IOException, InterruptedException {
        List<HomeWork> homeWorks = new ArrayList<>();
        for(HomeWorkContent homeWorkContent : homeWorksContent) {
            HomeWork homeWork = new HomeWork();
            List<AddCheckWithStudent> addCheckWithStudents = new ArrayList<>();
            HomeWork homeWorkCheck = homeWorkRepository.findByTitle(homeWorkContent.getTitle()).orElse(null);
//            if(homeWorkCheck == null) {
//                if(homeWorkContent.getUsers().size() >= 2){
//                    addCheckWithStudents = createChecks(homeWorkContent.getUsers());
//                }
//            }
//            else{
//                addCheckWithStudents = createChecks(getUncheckedStudent(homeWorkCheck.getCheckSimilarities(),
//                        homeWorkContent.getUsers()));
//                homeWork.setId(homeWorkCheck.getId());
//            }


            addCheckWithStudents = createChecks(homeWorkContent.getUsers());
            List<CheckSimilarity> checkSimilarities = checkMapper.toCheckSimilarities(addCheckWithStudents);
            if(homeWorkCheck != null) {
                homeWork.setTitle(homeWorkCheck.getTitle());
                homeWork.setId(homeWorkCheck.getId());
            }else{
                homeWork.setTitle(homeWorkContent.getTitle());
            }
            homeWork.setType(HomeWorkType.PYTHON);
            checkSimilarities.forEach(o -> {
                CheckSimilarity checkSimilarity = checkService.find((o.getStudent1() != null)? o.getStudent1().getId():0,
                        o.getStudent2() != null? o.getStudent2().getId():0,
                homeWorkCheck != null? homeWorkCheck.getId():0);
                System.out.println("arguments");
                System.out.println((o.getStudent1() != null)? o.getStudent1().getId():0);
                System.out.println(o.getStudent2() != null? o.getStudent2().getId():0);
                System.out.println(homeWorkCheck != null? homeWorkCheck.getId():0);

                if(checkSimilarity != null) {
                    o.setId(checkSimilarity.getId());
                    System.out.println("update");
                }
                o.setHomeWork(homeWork);
                System.out.println("-----------------------------------------");
            });
            homeWork.setCheckSimilarities(checkSimilarities);
            homeWorks.add(homeWork);
            System.out.println("home work created");

        }
        System.out.println("home works created");

        return homeWorkRepository.saveAll(homeWorks);
    }

    private List<AddCheckWithStudent> createChecks(List<UserContent> userContents) throws IOException, InterruptedException {
        List<AddCheckWithStudent> checks = new ArrayList<>();
        for (int i = 0; i < userContents.size(); i++) {
            for (int j = i + 1; j < userContents.size(); j++) {
                AddCheckWithStudent check = new AddCheckWithStudent();
                double similarity = calculateService.calculate(userContents.get(i).getContent(),
                        userContents.get(j).getContent());


                Student student1;
                Student student2;
                List<Student> students1 = studentService.getStudentsByName(userContents.get(i).getFirstName(),
                        userContents.get(i).getLastName());

                List<Student> students2 = studentService.getStudentsByName(userContents.get(j).getFirstName(),
                        userContents.get(j).getLastName());


                if (students1.isEmpty()){
                    student1 = new Student();
                    student1.setFirstName(userContents.get(i).getFirstName());
                    student1.setLastName(userContents.get(i).getLastName());
                    student1 = studentService.addStudent(student1);
                }else {
                    student1 = students1.get(0);
                }

                if (students2.isEmpty()){
                    student2 = new Student();
                    student2.setFirstName(userContents.get(j).getFirstName());
                    student2.setLastName(userContents.get(j).getLastName());
                    student2 = studentService.addStudent(student2);
                }else {
                    student2 = students2.get(0);
                }

                check.setStudent1_id(student1.getId());
                check.setStudent2_id(student2.getId());
                check.setSimilarity(similarity);
                checks.add(check);
                System.out.println("add check " + check.getStudent1_id() + " " + check.getStudent2_id() + " "  + check.getSimilarity());
            }
        }

        return checks;
    }


    private List<UserContent> getUncheckedStudent(List<CheckSimilarity> checkSimilarities,
                                                            List<UserContent> userContents) throws IOException, InterruptedException {
        Set<StudentDto> checkedStudents = new HashSet<>();
        List<UserContent> uncheckedStudents = new ArrayList<>();

        for (CheckSimilarity checkSimilarity : checkSimilarities) {
            StudentDto student = new StudentDto();
            student.setFirstName(checkSimilarity.getStudent1().getFirstName());
            student.setLastName(checkSimilarity.getStudent1().getLastName());
            checkedStudents.add(student);
        }
        for (UserContent userContent : userContents) {
            StudentDto student = new StudentDto();
            student.setFirstName(userContent.getFirstName());
            student.setLastName(userContent.getLastName());
            if(checkedStudents.add(student)) {
                uncheckedStudents.add(userContent);
            }
        }

        return uncheckedStudents;

    }
}
