package com.example.school_managementv1.repository;

import com.example.school_managementv1.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
}
