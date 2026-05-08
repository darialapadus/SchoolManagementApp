package com.example.school_managementv1.controller;

import com.example.school_managementv1.dto.StudentCreateRequest;
import com.example.school_managementv1.dto.StudentUpdateRequest;
import com.example.school_managementv1.entity.Student;
import com.example.school_managementv1.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@Tag(name = "Students", description = "CRUD operations for students")
@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @Operation(summary = "Create a student")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Student created"),
            @ApiResponse(responseCode = "400", description = "Validation error / duplicate email"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PostMapping
    public ResponseEntity<Student> create(@Valid @RequestBody StudentCreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(studentService.create(req));
    }

    @Operation(summary = "Get all students (no pagination)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of students returned")
    })
    @GetMapping
    public List<Student> getAll() {
        return studentService.findAll();
    }

    @Operation(summary = "Get students paginated and sorted",
            description = "Params: page (0-based), size (default 10), sort (e.g. lastName,asc or gradeLevel,desc)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of students returned")
    })
    @GetMapping("/paged")
    public Page<Student> getAllPaged(
            @PageableDefault(size = 10, sort = "lastName", direction = Sort.Direction.ASC) Pageable pageable) {
        return studentService.findAllPaged(pageable);
    }

    @Operation(summary = "Get student by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student found"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @GetMapping("/{id}")
    public Student getById(@PathVariable Long id) {
        return studentService.findById(id);
    }

    @Operation(summary = "Update a student")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Student updated"),
            @ApiResponse(responseCode = "400", description = "Validation error / duplicate email"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @PutMapping("/{id}")
    public Student update(@PathVariable Long id,
                          @Valid @RequestBody StudentUpdateRequest req) {
        return studentService.update(id, req);
    }

    @Operation(summary = "Delete a student")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Student deleted"),
            @ApiResponse(responseCode = "404", description = "Student not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
