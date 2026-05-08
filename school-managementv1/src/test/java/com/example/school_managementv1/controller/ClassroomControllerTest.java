package com.example.school_managementv1.controller;

import com.example.school_managementv1.dto.ClassroomCreateRequest;
import com.example.school_managementv1.dto.ClassroomUpdateRequest;
import com.example.school_managementv1.entity.Classroom;
import com.example.school_managementv1.service.ClassroomService;
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

@WebMvcTest(value = ClassroomController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
class ClassroomControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockitoBean ClassroomService classroomService;

    @Test
    void create_returns201() throws Exception {
        ClassroomCreateRequest req = new ClassroomCreateRequest("A", "101", 30);
        Classroom c = new Classroom();c.setId(1L);

        when(classroomService.create(req)).thenReturn(c);

        mockMvc.perform(post("/api/classrooms")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getAll_returns200() throws Exception {
        mockMvc.perform(get("/api/classrooms"))
                .andExpect(status().isOk());
    }

    @Test
    void update_returns200() throws Exception {
        ClassroomUpdateRequest req = new ClassroomUpdateRequest("A", "102", 40);
        Classroom c = new Classroom();c.setId(1L);

        when(classroomService.update(1L, req)).thenReturn(c);

        mockMvc.perform(put("/api/classrooms/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/api/classrooms/1"))
                .andExpect(status().isNoContent());
    }
}
