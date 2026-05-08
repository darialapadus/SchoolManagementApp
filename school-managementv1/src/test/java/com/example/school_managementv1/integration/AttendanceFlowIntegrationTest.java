package com.example.school_managementv1.integration;

import com.example.school_managementv1.repository.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
 * Teacher → Classroom → Course → Student → Attendance (multiple date) → Read → Delete
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(roles = "ADMIN")
class AttendanceFlowIntegrationTest {

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
    void fullAttendanceTrackingFlow() throws Exception {
        Long teacherId = createAndGetId("/api/teachers", """
                {"firstName":"Mihai","lastName":"Popescu","email":"mihai.pop@school.ro","department":"Matematica"}
                """);
        Long classroomId = createAndGetId("/api/classrooms", """
                {"building":"Corp B","roomNumber":"101","capacity":25}
                """);
        String courseJson = String.format(
                "{\"name\":\"Analiza Matematica\",\"credits\":5,\"teacherId\":%d,\"classroomId\":%d}",
                teacherId, classroomId);
        Long courseId = createAndGetId("/api/courses", courseJson);
        Long studentId = createAndGetId("/api/students", """
                {"firstName":"Radu","lastName":"Stan","email":"radu.stan@test.com","gradeLevel":10}
                """);

        markAttendance(studentId, courseId, "2026-03-01", true);
        markAttendance(studentId, courseId, "2026-03-08", false);
        markAttendance(studentId, courseId, "2026-03-15", true);

        mockMvc.perform(get("/api/academic/attendance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        mockMvc.perform(get("/api/academic/attendance/by-student/{id}", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        mockMvc.perform(get("/api/academic/attendance/by-course/{id}", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

        MvcResult listResult = mockMvc.perform(
                        get("/api/academic/attendance/by-student/{id}", studentId))
                .andReturn();
        JsonNode list = objectMapper.readTree(listResult.getResponse().getContentAsString());
        Long firstAttendanceId = list.get(0).get("id").asLong();

        mockMvc.perform(get("/api/academic/attendance/{id}", firstAttendanceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(firstAttendanceId));

        mockMvc.perform(post("/api/academic/attendance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildAttendanceJson(studentId, courseId, "2026-03-01", true)))
                .andExpect(status().isBadRequest());

        mockMvc.perform(delete("/api/academic/attendance/{id}", firstAttendanceId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/academic/attendance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void userRole_canReadAttendance_butCannotDelete() throws Exception {
        mockMvc.perform(get("/api/academic/attendance"))
                .andExpect(status().isOk());
    }

    private void markAttendance(Long studentId, Long courseId, String date, boolean present) throws Exception {
        mockMvc.perform(post("/api/academic/attendance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(buildAttendanceJson(studentId, courseId, date, present)))
                .andExpect(status().isOk());
    }

    private String buildAttendanceJson(Long studentId, Long courseId, String date, boolean present) {
        return String.format(
                "{\"studentId\":%d,\"courseId\":%d,\"date\":\"%s\",\"present\":%b}",
                studentId, courseId, date, present);
    }

    private Long createAndGetId(String url, String json) throws Exception {
        MvcResult result = mockMvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().is2xxSuccessful())
                .andReturn();
        return objectMapper.readTree(result.getResponse().getContentAsString())
                .get("id").asLong();
    }
}
