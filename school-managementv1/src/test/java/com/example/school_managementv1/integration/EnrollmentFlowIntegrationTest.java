package com.example.school_managementv1.integration;

import com.example.school_managementv1.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Teacher → Classroom → Course → Student → Enrollment → Grade
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(roles = "ADMIN")
class EnrollmentFlowIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @Autowired GradeRepository gradeRepository;
    @Autowired AttendanceRepository attendanceRepository;
    @Autowired EnrollmentRepository enrollmentRepository;
    @Autowired CourseRepository courseRepository;
    @Autowired StudentRepository studentRepository;
    @Autowired TeacherRepository teacherRepository;
    @Autowired ClassroomRepository classroomRepository;

    @BeforeEach
    void setUp() {
        gradeRepository.deleteAll();
        attendanceRepository.deleteAll();
        enrollmentRepository.deleteAll();
        courseRepository.deleteAll();
        studentRepository.deleteAll();
        teacherRepository.deleteAll();
        classroomRepository.deleteAll();
    }

    @Test
    void fullEnrollmentAndGradingFlow() throws Exception {
        Long teacherId = createAndGetId("/api/teachers", """
                {"firstName":"Alexandru","lastName":"Marin","email":"alex.marin@school.ro","department":"Informatica"}
                """);

        Long classroomId = createAndGetId("/api/classrooms", """
                {"building":"Corp A","roomNumber":"201","capacity":30}
                """);

        String courseJson = String.format(
                "{\"name\":\"Algoritmi\",\"credits\":6,\"teacherId\":%d,\"classroomId\":%d}",
                teacherId, classroomId);
        Long courseId = createAndGetId("/api/courses", courseJson);

        Long studentId = createAndGetId("/api/students", """
                {"firstName":"Elena","lastName":"Dumitrescu","email":"elena.d@test.com","gradeLevel":11}
                """);

        String enrollJson = String.format(
                "{\"studentId\":%d,\"courseId\":%d}", studentId, courseId);
        Long enrollmentId = createAndGetId("/api/enrollments", enrollJson);

        mockMvc.perform(get("/api/enrollments/by-student/{id}", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(enrollmentId));

        String gradeJson = String.format(
                "{\"enrollmentId\":%d,\"value\":9}", enrollmentId);
        mockMvc.perform(post("/api/academic/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gradeJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(9));

        mockMvc.perform(get("/api/academic/grades"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].value").value(9));

        mockMvc.perform(post("/api/academic/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("{\"enrollmentId\":%d,\"value\":10}", enrollmentId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(10));

        mockMvc.perform(post("/api/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(enrollJson))
                .andExpect(status().isBadRequest());
    }

    private Long createAndGetId(String url, String json) throws Exception {
        MvcResult result = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        JsonNode node = objectMapper.readTree(result.getResponse().getContentAsString());
        return node.get("id").asLong();
    }
}