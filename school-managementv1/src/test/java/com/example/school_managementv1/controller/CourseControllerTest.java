package com.example.school_managementv1.controller;

import com.example.school_managementv1.dto.CourseCreateRequest;
import com.example.school_managementv1.entity.Course;
import com.example.school_managementv1.service.CourseService;
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

@WebMvcTest(value = CourseController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
class CourseControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockitoBean CourseService courseService;

    @Test
    void create_returns201() throws Exception {
        CourseCreateRequest req = new CourseCreateRequest("Math", 5, 1L, 1L);
        Course c = new Course();c.setId(1L);

        when(courseService.create(req)).thenReturn(c);

        mockMvc.perform(post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getAll_returns200() throws Exception {
        when(courseService.findAll()).thenReturn(List.of(new Course()));

        mockMvc.perform(get("/api/courses"))
                .andExpect(status().isOk());
    }

    @Test
    void getById_returns200() throws Exception {
        Course c = new Course();c.setId(9L);
        when(courseService.findById(9L)).thenReturn(c);

        mockMvc.perform(get("/api/courses/9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(9));
    }
}
