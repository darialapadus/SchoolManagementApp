package com.example.school_managementv1.service;

import com.example.school_managementv1.dto.StudentCreateRequest;
import com.example.school_managementv1.dto.StudentUpdateRequest;
import com.example.school_managementv1.entity.Student;
import com.example.school_managementv1.exception.BadRequestException;
import com.example.school_managementv1.exception.NotFoundException;
import com.example.school_managementv1.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock StudentRepository studentRepository;
    @InjectMocks StudentService studentService;

    @Test
    void create_success() {
        StudentCreateRequest req = new StudentCreateRequest("Ana", "Popescu", "ana@test.com", 10);

        when(studentRepository.existsByEmail(req.email())).thenReturn(false);
        when(studentRepository.save(any(Student.class))).thenAnswer(inv -> inv.getArgument(0));

        Student saved = studentService.create(req);

        assertEquals("Ana", saved.getFirstName());
        assertEquals("Popescu", saved.getLastName());
        assertEquals("ana@test.com", saved.getEmail());
        assertEquals(10, saved.getGradeLevel());
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void create_duplicateEmail_throwsBadRequest() {
        StudentCreateRequest req = new StudentCreateRequest("Ana", "Popescu", "ana@test.com", 10);
        when(studentRepository.existsByEmail(req.email())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> studentService.create(req));
        verify(studentRepository, never()).save(any());
    }

    @Test
    void findAll_returnsList() {
        when(studentRepository.findAll()).thenReturn(List.of(new Student(), new Student()));
        assertEquals(2, studentService.findAll().size());
    }

    @Test
    void findById_notFound_throws() {
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> studentService.findById(1L));
    }

    @Test
    void update_changeEmailToExisting_throwsBadRequest() {
        Student existing = new Student();
        existing.setId(1L);
        existing.setFirstName("Ana");
        existing.setLastName("Popescu");
        existing.setEmail("old@test.com");
        existing.setGradeLevel(10);

        StudentUpdateRequest req = new StudentUpdateRequest("Ana", "Popescu", "new@test.com", 11);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(studentRepository.existsByEmail("new@test.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> studentService.update(1L, req));
        verify(studentRepository, never()).save(any());
    }

    @Test
    void delete_success() {
        Student existing = new Student();
        existing.setId(1L);
        existing.setEmail("x@test.com");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(existing));

        studentService.delete(1L);

        verify(studentRepository).delete(existing);
    }
}
