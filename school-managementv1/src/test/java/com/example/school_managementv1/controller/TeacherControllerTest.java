package com.example.school_managementv1.controller;

import com.example.school_managementv1.dto.TeacherCreateRequest;
import com.example.school_managementv1.dto.TeacherUpdateRequest;
import com.example.school_managementv1.entity.Teacher;
import com.example.school_managementv1.service.TeacherService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = TeacherController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
class TeacherControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockitoBean TeacherService teacherService;

    @Test
    void create_returns201() throws Exception {
        TeacherCreateRequest req = new TeacherCreateRequest("Ion", "Ionescu", "ion@school.ro", "Math");

        Teacher t = new Teacher();
        t.setId(1L);
        t.setEmail("ion@school.ro");
        when(teacherService.create(req)).thenReturn(t);

        mockMvc.perform(post("/api/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("ion@school.ro"));
    }

    @Test
    void getAll_returns200() throws Exception {
        mockMvc.perform(get("/api/teachers"))
                .andExpect(status().isOk());
    }

    @Test
    void getById_returns200() throws Exception {
        Teacher t = new Teacher();
        t.setId(3L);
        when(teacherService.findById(3L)).thenReturn(t);

        mockMvc.perform(get("/api/teachers/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(3));
    }

    @Test
    void update_returns200() throws Exception {
        TeacherUpdateRequest req = new TeacherUpdateRequest("Ion", "Ionescu", "ion@school.ro", "IT");
        Teacher t = new Teacher();
        t.setId(1L);
        when(teacherService.update(1L, req)).thenReturn(t);

        mockMvc.perform(put("/api/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/api/teachers/1"))
                .andExpect(status().isNoContent());
    }
}
