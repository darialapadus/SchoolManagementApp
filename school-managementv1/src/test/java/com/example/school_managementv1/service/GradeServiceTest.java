package com.example.school_managementv1.service;

import com.example.school_managementv1.dto.GradeRequest;
import com.example.school_managementv1.entity.Enrollment;
import com.example.school_managementv1.entity.Grade;
import com.example.school_managementv1.exception.NotFoundException;
import com.example.school_managementv1.repository.EnrollmentRepository;
import com.example.school_managementv1.repository.GradeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GradeServiceTest {

    @Mock GradeRepository gradeRepository;
    @Mock EnrollmentRepository enrollmentRepository;
    @InjectMocks GradeService gradeService;

    @Test
    void setGrade_enrollmentNotFound_throws() {
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> gradeService.setGrade(new GradeRequest(1L, 9)));
    }

    @Test
    void setGrade_createsNewGrade() {
        Enrollment e = new Enrollment(); e.setId(1L);

        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(e));
        when(gradeRepository.findByEnrollmentId(1L)).thenReturn(Optional.empty());
        when(gradeRepository.save(any(Grade.class))).thenAnswer(inv -> inv.getArgument(0));

        Grade saved = gradeService.setGrade(new GradeRequest(1L, 10));

        assertEquals(e, saved.getEnrollment());
        assertEquals(10, saved.getValue());
        assertNotNull(saved.getGradedAt());
    }
}
