package com.example.school_managementv1.controller;

import com.example.school_managementv1.dto.CourseCreateRequest;
import com.example.school_managementv1.dto.CourseUpdateRequest;
import com.example.school_managementv1.entity.Course;
import com.example.school_managementv1.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Courses", description = "CRUD operations for courses")
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @Operation(summary = "Create a course")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Course created"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Teacher/Classroom not found")
    })
    @PostMapping
    public ResponseEntity<Course> create(@Valid @RequestBody CourseCreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(courseService.create(req));
    }

    @Operation(summary = "Get all courses (no pagination)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of courses returned")
    })
    @GetMapping
    public List<Course> getAll() {
        return courseService.findAll();
    }

    @Operation(summary = "Get courses paginated and sorted",
            description = "Params: page (0-based), size (default 10), sort (e.g. name,asc or credits,desc)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of courses returned")
    })
    @GetMapping("/paged")
    public Page<Course> getAllPaged(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return courseService.findAllPaged(pageable);
    }

    @Operation(summary = "Get course by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course found"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @GetMapping("/{id}")
    public Course getById(@PathVariable Long id) {
        return courseService.findById(id);
    }

    @Operation(summary = "Update a course")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Course updated"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Course/Teacher/Classroom not found")
    })
    @PutMapping("/{id}")
    public Course update(@PathVariable Long id, @Valid @RequestBody CourseUpdateRequest req) {
        return courseService.update(id, req);
    }

    @Operation(summary = "Delete a course")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Course deleted"),
            @ApiResponse(responseCode = "400", description = "Course has active enrollments"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        courseService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
