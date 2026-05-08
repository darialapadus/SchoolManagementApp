package com.example.school_managementv1.service;

import com.example.school_managementv1.dto.AttendanceRequest;
import com.example.school_managementv1.entity.Attendance;
import com.example.school_managementv1.entity.Course;
import com.example.school_managementv1.entity.Student;
import com.example.school_managementv1.exception.BadRequestException;
import com.example.school_managementv1.exception.NotFoundException;
import com.example.school_managementv1.repository.AttendanceRepository;
import com.example.school_managementv1.repository.CourseRepository;
import com.example.school_managementv1.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AttendanceServiceTest {

    @Mock AttendanceRepository attendanceRepository;
    @Mock StudentRepository studentRepository;
    @Mock CourseRepository courseRepository;
    @InjectMocks AttendanceService attendanceService;

    @Test
    void mark_duplicate_throws() {
        LocalDate d = LocalDate.of(2026, 1, 6);
        AttendanceRequest req = new AttendanceRequest(1L, 2L, d, true);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student()));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(new Course()));
        when(attendanceRepository.existsByStudentIdAndCourseIdAndDate(1L, 2L, d)).thenReturn(true);

        assertThrows(BadRequestException.class, () -> attendanceService.mark(req));
    }

    @Test
    void mark_success() {
        LocalDate d = LocalDate.of(2026, 1, 6);
        AttendanceRequest req = new AttendanceRequest(1L, 2L, d, true);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(new Student()));
        when(courseRepository.findById(2L)).thenReturn(Optional.of(new Course()));
        when(attendanceRepository.existsByStudentIdAndCourseIdAndDate(1L, 2L, d)).thenReturn(false);
        when(attendanceRepository.save(any(Attendance.class))).thenAnswer(inv -> inv.getArgument(0));

        Attendance saved = attendanceService.mark(req);
        assertEquals(d, saved.getDate());
        assertTrue(saved.getPresent());
    }

    @Test
    void mark_studentNotFound_throws() {
        LocalDate d = LocalDate.of(2026, 1, 6);
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> attendanceService.mark(new AttendanceRequest(1L, 2L, d, true)));
    }
}
