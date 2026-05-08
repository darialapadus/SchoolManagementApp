package com.example.school_managementv1.controller;

import com.example.school_managementv1.dto.EnrollmentCreateRequest;
import com.example.school_managementv1.entity.Enrollment;
import com.example.school_managementv1.service.EnrollmentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = EnrollmentController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
class EnrollmentControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockitoBean EnrollmentService enrollmentService;

    @Test
    void enroll_returns201() throws Exception {
        EnrollmentCreateRequest req = new EnrollmentCreateRequest(1L,1L);
        Enrollment e = new Enrollment(); e.setId(1L);

        when(enrollmentService.enroll(req)).thenReturn(e);

        mockMvc.perform(post("/api/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void byStudent_returns200() throws Exception {
        when(enrollmentService.listByStudent(1L)).thenReturn(List.of(new Enrollment()));

        mockMvc.perform(get("/api/enrollments/by-student/1"))
                .andExpect(status().isOk());
    }

    @Test
    void byCourse_returns200() throws Exception {
        when(enrollmentService.listByCourse(1L)).thenReturn(List.of(new Enrollment()));

        mockMvc.perform(get("/api/enrollments/by-course/1"))
                .andExpect(status().isOk());
    }
}
