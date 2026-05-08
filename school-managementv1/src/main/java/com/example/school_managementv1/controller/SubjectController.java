package com.example.school_managementv1.controller;

import com.example.school_managementv1.dto.SubjectCreateRequest;
import com.example.school_managementv1.dto.SubjectUpdateRequest;
import com.example.school_managementv1.entity.Subject;
import com.example.school_managementv1.entity.Teacher;
import com.example.school_managementv1.service.SubjectService;
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

@Tag(name = "Subjects", description = "CRUD for subjects and teacher-subject assignments (ManyToMany)")
@RestController
@RequestMapping("/api/subjects")
public class SubjectController {

    private final SubjectService subjectService;

    public SubjectController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Operation(summary = "Create a subject")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Subject created"),
            @ApiResponse(responseCode = "400", description = "Name already exists / validation error")
    })
    @PostMapping
    public ResponseEntity<Subject> create(@Valid @RequestBody SubjectCreateRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subjectService.create(req));
    }

    @Operation(summary = "Get all subjects (no pagination)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subjects returned")
    })
    @GetMapping
    public List<Subject> getAll() {
        return subjectService.findAll();
    }

    @Operation(summary = "Get subjects paginated and sorted",
            description = "Params: page (0-based), size (default 10), sort (e.g. name,asc or description,desc)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of subjects returned")
    })
    @GetMapping("/paged")
    public Page<Subject> getAllPaged(
            @PageableDefault(size = 10, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        return subjectService.findAllPaged(pageable);
    }

    @Operation(summary = "Get subject by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subject found"),
            @ApiResponse(responseCode = "404", description = "Subject not found")
    })
    @GetMapping("/{id}")
    public Subject getById(@PathVariable Long id) {
        return subjectService.findById(id);
    }

    @Operation(summary = "Update a subject")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subject updated"),
            @ApiResponse(responseCode = "400", description = "Name already exists / validation error"),
            @ApiResponse(responseCode = "404", description = "Subject not found")
    })
    @PutMapping("/{id}")
    public Subject update(@PathVariable Long id, @Valid @RequestBody SubjectUpdateRequest req) {
        return subjectService.update(id, req);
    }

    @Operation(summary = "Delete a subject")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Subject deleted"),
            @ApiResponse(responseCode = "404", description = "Subject not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subjectService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Assign a subject to a teacher (ManyToMany)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subject assigned to teacher"),
            @ApiResponse(responseCode = "404", description = "Subject or Teacher not found")
    })
    @PostMapping("/{subjectId}/teachers/{teacherId}")
    public Teacher assignToTeacher(@PathVariable Long subjectId, @PathVariable Long teacherId) {
        return subjectService.assignSubjectToTeacher(subjectId, teacherId);
    }

    @Operation(summary = "Remove a subject from a teacher (ManyToMany)")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subject removed from teacher"),
            @ApiResponse(responseCode = "404", description = "Subject or Teacher not found")
    })
    @DeleteMapping("/{subjectId}/teachers/{teacherId}")
    public Teacher removeFromTeacher(@PathVariable Long subjectId, @PathVariable Long teacherId) {
        return subjectService.removeSubjectFromTeacher(subjectId, teacherId);
    }
}
