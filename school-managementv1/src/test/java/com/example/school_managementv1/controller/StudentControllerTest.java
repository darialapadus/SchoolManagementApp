package com.example.school_managementv1.controller;

import com.example.school_managementv1.dto.StudentCreateRequest;
import com.example.school_managementv1.dto.StudentUpdateRequest;
import com.example.school_managementv1.entity.Student;
import com.example.school_managementv1.service.StudentService;
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

@WebMvcTest(value = StudentController.class,
        excludeAutoConfiguration = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
class StudentControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @MockitoBean StudentService studentService;

    @Test
    void create_returns201() throws Exception {
        StudentCreateRequest req = new StudentCreateRequest("Ana", "Popescu", "ana@test.com", 10);

        Student s = new Student();
        s.setId(1L);
        s.setFirstName("Ana");
        s.setLastName("Popescu");
        s.setEmail("ana@test.com");
        s.setGradeLevel(10);

        when(studentService.create(req)).thenReturn(s);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("ana@test.com"));
    }

    @Test
    void getAll_returns200() throws Exception {
        when(studentService.findAll()).thenReturn(List.of(new Student(), new Student()));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk());
    }

    @Test
    void getById_returns200() throws Exception {
        Student s = new Student();
        s.setId(5L);
        when(studentService.findById(5L)).thenReturn(s);

        mockMvc.perform(get("/api/students/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5));
    }

    @Test
    void update_returns200() throws Exception {
        StudentUpdateRequest req = new StudentUpdateRequest("Ana", "Ionescu", "ana@test.com", 11);

        Student s = new Student();
        s.setId(1L);
        s.setEmail("ana@test.com");
        when(studentService.update(1L, req)).thenReturn(s);

        mockMvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void delete_returns204() throws Exception {
        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());
    }
}
