package com.example.school_managementv1.repository;

import com.example.school_managementv1.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    Optional<Grade> findByEnrollmentId(Long enrollmentId);
    boolean existsByEnrollmentId(Long enrollmentId);
}
