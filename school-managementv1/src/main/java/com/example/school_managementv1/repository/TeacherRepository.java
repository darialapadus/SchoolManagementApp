package com.example.school_managementv1.repository;

import com.example.school_managementv1.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByEmail(String email);
    boolean existsByEmail(String email);
}
