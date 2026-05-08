package com.example.school_managementv1.controller;

import com.example.school_managementv1.dto.StudentCreateRequest;
import com.example.school_managementv1.dto.StudentUpdateRequest;
import com.example.school_managementv1.exception.BadRequestException;
import com.example.school_managementv1.service.StudentService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/students")
public class StudentWebController {

    private final StudentService studentService;

    public StudentWebController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "lastName") String sort,
                       @RequestParam(defaultValue = "asc") String dir,
                       Model model) {
        Sort sorting = dir.equals("desc") ? Sort.by(sort).descending() : Sort.by(sort).ascending();
        model.addAttribute("page", studentService.findAllPaged(PageRequest.of(page, size, sorting)));
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("size", size);
        return "students/list";
    }

    @GetMapping("/new")
    public String newForm() {
        return "students/form";
    }

    @PostMapping
    public String create(@RequestParam String firstName,
                         @RequestParam String lastName,
                         @RequestParam String email,
                         @RequestParam Integer gradeLevel,
                         RedirectAttributes ra) {
        try {
            studentService.create(new StudentCreateRequest(firstName.trim(), lastName.trim(), email.trim(), gradeLevel));
            ra.addFlashAttribute("success", "Student added successfully.");
            return "redirect:/web/students";
        } catch (BadRequestException e) {
            ra.addFlashAttribute("error", e.getMessage());
            ra.addFlashAttribute("firstName", firstName);
            ra.addFlashAttribute("lastName", lastName);
            ra.addFlashAttribute("email", email);
            ra.addFlashAttribute("gradeLevel", gradeLevel);
            return "redirect:/web/students/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        var s = studentService.findById(id);
        model.addAttribute("student", s);
        model.addAttribute("isEdit", true);
        return "students/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String firstName,
                         @RequestParam String lastName,
                         @RequestParam String email,
                         @RequestParam Integer gradeLevel,
                         RedirectAttributes ra) {
        try {
            studentService.update(id, new StudentUpdateRequest(firstName.trim(), lastName.trim(), email.trim(), gradeLevel));
            ra.addFlashAttribute("success", "Student updated successfully.");
            return "redirect:/web/students";
        } catch (BadRequestException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/web/students/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            studentService.delete(id);
            ra.addFlashAttribute("success", "Student deleted successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Could not delete student: " + e.getMessage());
        }
        return "redirect:/web/students";
    }
}
