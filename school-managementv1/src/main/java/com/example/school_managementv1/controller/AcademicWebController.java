package com.example.school_managementv1.controller;

import com.example.school_managementv1.dto.AttendanceRequest;
import com.example.school_managementv1.dto.GradeRequest;
import com.example.school_managementv1.exception.BadRequestException;
import com.example.school_managementv1.service.AttendanceService;
import com.example.school_managementv1.service.CourseService;
import com.example.school_managementv1.service.EnrollmentService;
import com.example.school_managementv1.service.GradeService;
import com.example.school_managementv1.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class AcademicWebController {

    private final GradeService gradeService;
    private final AttendanceService attendanceService;
    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final CourseService courseService;

    public AcademicWebController(GradeService gradeService,
                                  AttendanceService attendanceService,
                                  EnrollmentService enrollmentService,
                                  StudentService studentService,
                                  CourseService courseService) {
        this.gradeService = gradeService;
        this.attendanceService = attendanceService;
        this.enrollmentService = enrollmentService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    // ── Grades ────────────────────────────────────────────────────────────────

    @GetMapping("/web/grades")
    public String gradesList(Model model) {
        model.addAttribute("grades", gradeService.findAll());
        model.addAttribute("enrollments", enrollmentService.findAll());
        return "academic/grades";
    }

    @PostMapping("/web/grades")
    public String setGrade(@RequestParam Long enrollmentId,
                           @RequestParam Integer value,
                           RedirectAttributes ra) {
        try {
            gradeService.setGrade(new GradeRequest(enrollmentId, value));
            ra.addFlashAttribute("success", "Grade saved successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/web/grades";
    }

    @PostMapping("/web/grades/{id}/delete")
    public String deleteGrade(@PathVariable Long id, RedirectAttributes ra) {
        try {
            gradeService.delete(id);
            ra.addFlashAttribute("success", "Grade deleted successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/web/grades";
    }

    // ── Attendance ────────────────────────────────────────────────────────────

    @GetMapping("/web/attendance")
    public String attendanceList(Model model) {
        model.addAttribute("attendances", attendanceService.findAll());
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("courses", courseService.findAll());
        return "academic/attendance";
    }

    @PostMapping("/web/attendance")
    public String markAttendance(@RequestParam Long studentId,
                                 @RequestParam Long courseId,
                                 @RequestParam String date,
                                 @RequestParam(defaultValue = "false") Boolean present,
                                 RedirectAttributes ra) {
        try {
            attendanceService.mark(new AttendanceRequest(studentId, courseId, LocalDate.parse(date), present));
            ra.addFlashAttribute("success", "Attendance recorded successfully.");
        } catch (BadRequestException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/web/attendance";
    }

    @PostMapping("/web/attendance/{id}/delete")
    public String deleteAttendance(@PathVariable Long id, RedirectAttributes ra) {
        try {
            attendanceService.delete(id);
            ra.addFlashAttribute("success", "Attendance record deleted successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/web/attendance";
    }
}
