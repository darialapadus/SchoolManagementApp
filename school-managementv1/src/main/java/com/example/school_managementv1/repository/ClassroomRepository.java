package com.example.school_managementv1.repository;

import com.example.school_managementv1.entity.Classroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClassroomRepository extends JpaRepository<Classroom, Long> {
    boolean existsByBuildingAndRoomNumber(String building, String roomNumber);
    Optional<Classroom> findByBuildingAndRoomNumber(String building, String roomNumber);
}
