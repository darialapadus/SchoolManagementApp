package com.example.school_managementv1.service;

import com.example.school_managementv1.dto.TeacherCreateRequest;
import com.example.school_managementv1.dto.TeacherUpdateRequest;
import com.example.school_managementv1.entity.Teacher;
import com.example.school_managementv1.exception.BadRequestException;
import com.example.school_managementv1.exception.NotFoundException;
import com.example.school_managementv1.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {

    @Mock TeacherRepository teacherRepository;
    @InjectMocks TeacherService teacherService;

    @Test
    void create_success() {
        TeacherCreateRequest req = new TeacherCreateRequest("Ion", "Ionescu", "ion@school.ro", "Math");

        when(teacherRepository.existsByEmail(req.email())).thenReturn(false);
        when(teacherRepository.save(any(Teacher.class))).thenAnswer(inv -> inv.getArgument(0));

        Teacher saved = teacherService.create(req);

        assertEquals("Ion", saved.getFirstName());
        assertEquals("Ionescu", saved.getLastName());
        assertEquals("ion@school.ro", saved.getEmail());
        assertEquals("Math", saved.getDepartment());
    }

    @Test
    void create_duplicateEmail_throws() {
        TeacherCreateRequest req = new TeacherCreateRequest("Ion", "Ionescu", "ion@school.ro", "Math");
        when(teacherRepository.existsByEmail(req.email())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> teacherService.create(req));
    }

    @Test
    void findById_notFound_throws() {
        when(teacherRepository.findById(7L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> teacherService.findById(7L));
    }

    @Test
    void delete_teacherWithCourses_throwsBadRequest() {
        Teacher t = new Teacher();
        t.setId(1L);
        t.getCourses().add(new com.example.school_managementv1.entity.Course());

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(t));

        assertThrows(BadRequestException.class, () -> teacherService.delete(1L));
        verify(teacherRepository, never()).delete(any());
    }
}
