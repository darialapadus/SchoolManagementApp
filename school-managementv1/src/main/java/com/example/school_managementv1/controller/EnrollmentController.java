package com.example.school_managementv1.controller;

import com.example.school_managementv1.dto.EnrollmentCreateRequest;
import com.example.school_managementv1.entity.Enrollment;
import com.example.school_managementv1.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Enrollments", description = "CRUD operations for enrollments")
@RestController
@RequestMapping("/api/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @Operation(summary = "Enroll a student to a course")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Enrollment created"),
            @ApiResponse(responseCode = "400", description = "Student already enrolled / validation error"),
            @ApiResponse(responseCode = "404", description = "Student/Course not found")
    })
    @PostMapping
    public ResponseEntity<Enrollment> enroll(@Valid @RequestBody EnrollmentCreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(enrollmentService.enroll(req));
    }

    @Operation(summary = "Get all enrollments")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Enrollments returned")
    })
    @GetMapping
    public List<Enrollment> getAll() {
        return enrollmentService.findAll();
    }

    @Operation(summary = "Get enrollment by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Enrollment found"),
            @ApiResponse(responseCode = "404", description = "Enrollment not found")
    })
    @GetMapping("/{id}")
    public Enrollment getById(@PathVariable Long id) {
        return enrollmentService.findById(id);
    }

    @Operation(summary = "List enrollments by student")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Enrollments returned")
    })
    @GetMapping("/by-student/{studentId}")
    public List<Enrollment> byStudent(@PathVariable Long studentId) {
        return enrollmentService.listByStudent(studentId);
    }

    @Operation(summary = "List enrollments by course")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Enrollments returned")
    })
    @GetMapping("/by-course/{courseId}")
    public List<Enrollment> byCourse(@PathVariable Long courseId) {
        return enrollmentService.listByCourse(courseId);
    }

    @Operation(summary = "Delete an enrollment")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Enrollment deleted"),
            @ApiResponse(responseCode = "404", description = "Enrollment not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        enrollmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
