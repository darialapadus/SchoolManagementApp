package com.example.school_managementv1.controller;

import com.example.school_managementv1.dto.TeacherCreateRequest;
import com.example.school_managementv1.dto.TeacherUpdateRequest;
import com.example.school_managementv1.entity.Teacher;
import com.example.school_managementv1.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Tag(name = "Teachers", description = "CRUD operations for teachers")
@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @Operation(summary = "Create a teacher")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Teacher created"),
            @ApiResponse(responseCode = "400", description = "Validation error / duplicate email")
    })
    @PostMapping
    public ResponseEntity<Teacher> create(@Valid @RequestBody TeacherCreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(teacherService.create(req));
    }

    @Operation(summary = "Get all teachers (no pagination)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of teachers returned")
    })
    @GetMapping
    public List<Teacher> getAll() {
        return teacherService.findAll();
    }

    @Operation(summary = "Get teachers paginated and sorted",
            description = "Params: page (0-based), size (default 10), sort (e.g. lastName,asc or department,desc)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of teachers returned")
    })
    @GetMapping("/paged")
    public Page<Teacher> getAllPaged(
            @PageableDefault(size = 10, sort = "lastName", direction = Sort.Direction.ASC) Pageable pageable) {
        return teacherService.findAllPaged(pageable);
    }

    @Operation(summary = "Get teacher by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Teacher found"),
            @ApiResponse(responseCode = "404", description = "Teacher not found")
    })
    @GetMapping("/{id}")
    public Teacher getById(@PathVariable Long id) {
        return teacherService.findById(id);
    }

    @Operation(summary = "Update a teacher")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Teacher updated"),
            @ApiResponse(responseCode = "400", description = "Validation error / duplicate email"),
            @ApiResponse(responseCode = "404", description = "Teacher not found")
    })
    @PutMapping("/{id}")
    public Teacher update(@PathVariable Long id,
                          @Valid @RequestBody TeacherUpdateRequest req) {
        return teacherService.update(id, req);
    }

    @Operation(summary = "Delete a teacher")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Teacher deleted"),
            @ApiResponse(responseCode = "400", description = "Cannot delete teacher with assigned courses"),
            @ApiResponse(responseCode = "404", description = "Teacher not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teacherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
