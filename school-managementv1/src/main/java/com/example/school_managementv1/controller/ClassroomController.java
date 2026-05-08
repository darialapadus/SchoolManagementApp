package com.example.school_managementv1.controller;

import com.example.school_managementv1.dto.ClassroomCreateRequest;
import com.example.school_managementv1.dto.ClassroomUpdateRequest;
import com.example.school_managementv1.entity.Classroom;
import com.example.school_managementv1.service.ClassroomService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Tag(name = "Classrooms", description = "CRUD operations for classrooms")
@RestController
@RequestMapping("/api/classrooms")
public class ClassroomController {

    private final ClassroomService classroomService;

    public ClassroomController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @Operation(summary = "Create a classroom")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Classroom created"),
            @ApiResponse(responseCode = "400", description = "Validation error / duplicate classroom")
    })
    @PostMapping
    public ResponseEntity<Classroom> create(@Valid @RequestBody ClassroomCreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(classroomService.create(req));
    }

    @Operation(summary = "Get all classrooms")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of classrooms returned")
    })
    @GetMapping
    public List<Classroom> getAll() {
        return classroomService.findAll();
    }

    @Operation(summary = "Get classroom by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Classroom found"),
            @ApiResponse(responseCode = "404", description = "Classroom not found")
    })
    @GetMapping("/{id}")
    public Classroom getById(@PathVariable Long id) {
        return classroomService.findById(id);
    }

    @Operation(summary = "Update a classroom")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Classroom updated"),
            @ApiResponse(responseCode = "400", description = "Validation error / duplicate classroom"),
            @ApiResponse(responseCode = "404", description = "Classroom not found")
    })
    @PutMapping("/{id}")
    public Classroom update(@PathVariable Long id,
                            @Valid @RequestBody ClassroomUpdateRequest req) {
        return classroomService.update(id, req);
    }

    @Operation(summary = "Delete a classroom")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Classroom deleted"),
            @ApiResponse(responseCode = "400", description = "Cannot delete classroom with assigned courses"),
            @ApiResponse(responseCode = "404", description = "Classroom not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        classroomService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
