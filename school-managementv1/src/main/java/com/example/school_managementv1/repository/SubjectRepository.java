package com.example.school_managementv1.repository;

import com.example.school_managementv1.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsByName(String name);
    Optional<Subject> findByName(String name);
}
