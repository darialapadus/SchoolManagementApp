package com.example.school_managementv1.repository;

import com.example.school_managementv1.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    boolean existsByStudentIdAndCourseIdAndDate(Long studentId, Long courseId, LocalDate date);
    Optional<Attendance> findByStudentIdAndCourseIdAndDate(Long studentId, Long courseId, LocalDate date);
    List<Attendance> findByStudentId(Long studentId);
    List<Attendance> findByCourseId(Long courseId);
}
