package com.example.school_managementv1.integration;

import com.example.school_managementv1.dto.StudentCreateRequest;
import com.example.school_managementv1.dto.StudentUpdateRequest;
import com.example.school_managementv1.repository.StudentRepository;
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
 * Student (Create → Read → Update → Delete).
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@WithMockUser(roles = "ADMIN")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;
    @Autowired StudentRepository studentRepository;

    private static Long createdId;

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
    }

    @Test
    @Order(1)
    void createStudent_thenGetById_thenUpdate_thenDelete() throws Exception {
        // 1. CREATE
        StudentCreateRequest createReq = new StudentCreateRequest(
                "Maria", "Ionescu", "maria.ionescu@test.com", 9);

        MvcResult createResult = mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReq)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value("Maria"))
                .andExpect(jsonPath("$.lastName").value("Ionescu"))
                .andExpect(jsonPath("$.email").value("maria.ionescu@test.com"))
                .andExpect(jsonPath("$.gradeLevel").value(9))
                .andReturn();

        Long id = objectMapper.readTree(createResult.getResponse().getContentAsString())
                .get("id").asLong();

        mockMvc.perform(get("/api/students/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("maria.ionescu@test.com"));

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        mockMvc.perform(get("/api/students/paged?page=0&size=5&sort=lastName,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.content", hasSize(1)));

        StudentUpdateRequest updateReq = new StudentUpdateRequest(
                "Maria", "Popescu", "maria.ionescu@test.com", 10);

        mockMvc.perform(put("/api/students/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Popescu"))
                .andExpect(jsonPath("$.gradeLevel").value(10));

        mockMvc.perform(delete("/api/students/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/students/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(2)
    void createStudent_duplicateEmail_returns400() throws Exception {
        StudentCreateRequest req = new StudentCreateRequest(
                "Ana", "Popa", "ana.popa@test.com", 8);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("BAD_REQUEST"));
    }

    @Test
    @Order(3)
    void createStudent_invalidData_returns400() throws Exception {
        String invalidJson = """
                {
                  "firstName": "X",
                  "lastName": "Y",
                  "email": "invalid-email",
                  "gradeLevel": 15
                }
                """;

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("VALIDATION_ERROR"));
    }
}
