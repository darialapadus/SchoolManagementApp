package com.example.school_managementv1.service;

import com.example.school_managementv1.dto.EnrollmentCreateRequest;
import com.example.school_managementv1.entity.Course;
import com.example.school_managementv1.entity.Enrollment;
import com.example.school_managementv1.entity.Student;
import com.example.school_managementv1.exception.BadRequestException;
import com.example.school_managementv1.exception.NotFoundException;
import com.example.school_managementv1.repository.CourseRepository;
import com.example.school_managementv1.repository.EnrollmentRepository;
import com.example.school_managementv1.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock EnrollmentRepository enrollmentRepository;
    @Mock StudentRepository studentRepository;
    @Mock CourseRepository courseRepository;
    @InjectMocks EnrollmentService enrollmentService;

    @Test
    void enroll_duplicate_throws() {
        EnrollmentCreateRequest req = new EnrollmentCreateRequest(1L, 2L);
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 2L)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> enrollmentService.enroll(req));
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void enroll_studentNotFound_throws() {
        EnrollmentCreateRequest req = new EnrollmentCreateRequest(1L, 2L);
        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 2L)).thenReturn(false);
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> enrollmentService.enroll(req));
    }

    @Test
    void enroll_success() {
        EnrollmentCreateRequest req = new EnrollmentCreateRequest(1L, 2L);

        when(enrollmentRepository.existsByStudentIdAndCourseId(1L, 2L)).thenReturn(false);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student()));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(new Course()));
        when(enrollmentRepository.save(any(Enrollment.class))).thenAnswer(inv -> inv.getArgument(0));

        Enrollment saved = enrollmentService.enroll(req);
        assertNotNull(saved.getEnrolledAt());
    }
}
