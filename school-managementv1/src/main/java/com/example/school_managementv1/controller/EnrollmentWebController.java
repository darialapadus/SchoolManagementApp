package com.example.school_managementv1.controller;

import com.example.school_managementv1.dto.EnrollmentCreateRequest;
import com.example.school_managementv1.exception.BadRequestException;
import com.example.school_managementv1.service.CourseService;
import com.example.school_managementv1.service.EnrollmentService;
import com.example.school_managementv1.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/enrollments")
public class EnrollmentWebController {

    private final EnrollmentService enrollmentService;
    private final StudentService studentService;
    private final CourseService courseService;

    public EnrollmentWebController(EnrollmentService enrollmentService,
                                   StudentService studentService,
                                   CourseService courseService) {
        this.enrollmentService = enrollmentService;
        this.studentService = studentService;
        this.courseService = courseService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("enrollments", enrollmentService.findAll());
        return "enrollments/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("students", studentService.findAll());
        model.addAttribute("courses", courseService.findAll());
        return "enrollments/form";
    }

    @PostMapping
    public String create(@RequestParam Long studentId,
                         @RequestParam Long courseId,
                         RedirectAttributes ra) {
        try {
            enrollmentService.enroll(new EnrollmentCreateRequest(studentId, courseId));
            ra.addFlashAttribute("success", "Student enrolled successfully.");
            return "redirect:/web/enrollments";
        } catch (BadRequestException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/web/enrollments/new";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            enrollmentService.delete(id);
            ra.addFlashAttribute("success", "Enrollment deleted successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Could not delete enrollment: " + e.getMessage());
        }
        return "redirect:/web/enrollments";
    }
}
