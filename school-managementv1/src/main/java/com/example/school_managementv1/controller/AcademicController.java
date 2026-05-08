package com.example.school_managementv1.controller;

import com.example.school_managementv1.dto.AttendanceRequest;
import com.example.school_managementv1.dto.GradeRequest;
import com.example.school_managementv1.entity.Attendance;
import com.example.school_managementv1.entity.Grade;
import com.example.school_managementv1.service.AttendanceService;
import com.example.school_managementv1.service.GradeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Academic", description = "CRUD operations for grades and attendance")
@RestController
@RequestMapping("/api/academic")
public class AcademicController {

    private final GradeService gradeService;
    private final AttendanceService attendanceService;

    public AcademicController(GradeService gradeService,
                              AttendanceService attendanceService) {
        this.gradeService = gradeService;
        this.attendanceService = attendanceService;
    }

    // ── Grades ────────────────────────────────────────────────────────────────

    @Operation(summary = "Set or update grade for an enrollment")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grade saved/updated"),
            @ApiResponse(responseCode = "400", description = "Validation error"),
            @ApiResponse(responseCode = "404", description = "Enrollment not found")
    })
    @PostMapping("/grades")
    public Grade setGrade(@Valid @RequestBody GradeRequest req) {
        return gradeService.setGrade(req);
    }

    @Operation(summary = "Get all grades")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grades returned")
    })
    @GetMapping("/grades")
    public List<Grade> getAllGrades() {
        return gradeService.findAll();
    }

    @Operation(summary = "Get grade by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Grade found"),
            @ApiResponse(responseCode = "404", description = "Grade not found")
    })
    @GetMapping("/grades/{id}")
    public Grade getGradeById(@PathVariable Long id) {
        return gradeService.findById(id);
    }

    @Operation(summary = "Delete a grade")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Grade deleted"),
            @ApiResponse(responseCode = "404", description = "Grade not found")
    })
    @DeleteMapping("/grades/{id}")
    public ResponseEntity<Void> deleteGrade(@PathVariable Long id) {
        gradeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // ── Attendance ────────────────────────────────────────────────────────────

    @Operation(summary = "Mark attendance for a student in a course on a date")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Attendance recorded"),
            @ApiResponse(responseCode = "400", description = "Already recorded / validation error"),
            @ApiResponse(responseCode = "404", description = "Student/Course not found")
    })
    @PostMapping("/attendance")
    public Attendance markAttendance(@Valid @RequestBody AttendanceRequest req) {
        return attendanceService.mark(req);
    }

    @Operation(summary = "Get all attendance records")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Attendance records returned")
    })
    @GetMapping("/attendance")
    public List<Attendance> getAllAttendance() {
        return attendanceService.findAll();
    }

    @Operation(summary = "Get attendance record by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Attendance found"),
            @ApiResponse(responseCode = "404", description = "Attendance not found")
    })
    @GetMapping("/attendance/{id}")
    public Attendance getAttendanceById(@PathVariable Long id) {
        return attendanceService.findById(id);
    }

    @Operation(summary = "Get attendance by student")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Attendance records returned")
    })
    @GetMapping("/attendance/by-student/{studentId}")
    public List<Attendance> getAttendanceByStudent(@PathVariable Long studentId) {
        return attendanceService.findByStudent(studentId);
    }

    @Operation(summary = "Get attendance by course")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Attendance records returned")
    })
    @GetMapping("/attendance/by-course/{courseId}")
    public List<Attendance> getAttendanceByCourse(@PathVariable Long courseId) {
        return attendanceService.findByCourse(courseId);
    }

    @Operation(summary = "Delete an attendance record")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Attendance deleted"),
            @ApiResponse(responseCode = "404", description = "Attendance not found")
    })
    @DeleteMapping("/attendance/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Long id) {
        attendanceService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
