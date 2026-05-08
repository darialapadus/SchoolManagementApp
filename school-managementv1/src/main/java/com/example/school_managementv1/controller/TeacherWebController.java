package com.example.school_managementv1.controller;

import com.example.school_managementv1.dto.TeacherCreateRequest;
import com.example.school_managementv1.dto.TeacherUpdateRequest;
import com.example.school_managementv1.exception.BadRequestException;
import com.example.school_managementv1.service.TeacherService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/teachers")
public class TeacherWebController {

    private final TeacherService teacherService;

    public TeacherWebController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "lastName") String sort,
                       @RequestParam(defaultValue = "asc") String dir,
                       Model model) {
        Sort sorting = dir.equals("desc") ? Sort.by(sort).descending() : Sort.by(sort).ascending();
        model.addAttribute("page", teacherService.findAllPaged(PageRequest.of(page, size, sorting)));
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("size", size);
        return "teachers/list";
    }

    @GetMapping("/new")
    public String newForm() {
        return "teachers/form";
    }

    @PostMapping
    public String create(@RequestParam String firstName,
                         @RequestParam String lastName,
                         @RequestParam String email,
                         @RequestParam String department,
                         RedirectAttributes ra) {
        try {
            teacherService.create(new TeacherCreateRequest(firstName.trim(), lastName.trim(), email.trim(), department.trim()));
            ra.addFlashAttribute("success", "Teacher added successfully.");
            return "redirect:/web/teachers";
        } catch (BadRequestException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/web/teachers/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("teacher", teacherService.findById(id));
        model.addAttribute("isEdit", true);
        return "teachers/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String firstName,
                         @RequestParam String lastName,
                         @RequestParam String email,
                         @RequestParam String department,
                         RedirectAttributes ra) {
        try {
            teacherService.update(id, new TeacherUpdateRequest(firstName.trim(), lastName.trim(), email.trim(), department.trim()));
            ra.addFlashAttribute("success", "Teacher updated successfully.");
            return "redirect:/web/teachers";
        } catch (BadRequestException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/web/teachers/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            teacherService.delete(id);
            ra.addFlashAttribute("success", "Teacher deleted successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Could not delete teacher: " + e.getMessage());
        }
        return "redirect:/web/teachers";
    }
}
