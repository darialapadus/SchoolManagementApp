package com.example.school_managementv1.controller;

import com.example.school_managementv1.dto.AttendanceRequest;
import com.example.school_managementv1.dto.GradeRequest;
import com.example.school_managementv1.entity.Attendance;
import com.example.school_managementv1.entity.Grade;
import com.example.school_managementv1.service.AttendanceService;
import com.example.school_managementv1.service.GradeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AcademicController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
class AcademicControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockitoBean GradeService gradeService;
    @MockitoBean AttendanceService attendanceService;

    @Test
    void setGrade_returns200() throws Exception {
        GradeRequest req = new GradeRequest(1L, 9);
        when(gradeService.setGrade(req)).thenReturn(new Grade());

        mockMvc.perform(post("/api/academic/grades")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void markAttendance_returns200() throws Exception {
        AttendanceRequest req = new AttendanceRequest(1L, 1L, LocalDate.of(2026, 1, 6), true);
        when(attendanceService.mark(req)).thenReturn(new Attendance());

        mockMvc.perform(post("/api/academic/attendance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }
}
