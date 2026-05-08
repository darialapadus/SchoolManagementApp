package com.example.school_managementv1.controller;

import com.example.school_managementv1.dto.ClassroomCreateRequest;
import com.example.school_managementv1.dto.ClassroomUpdateRequest;
import com.example.school_managementv1.exception.BadRequestException;
import com.example.school_managementv1.service.ClassroomService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/classrooms")
public class ClassroomWebController {

    private final ClassroomService classroomService;

    public ClassroomWebController(ClassroomService classroomService) {
        this.classroomService = classroomService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("classrooms", classroomService.findAll());
        return "classrooms/list";
    }

    @GetMapping("/new")
    public String newForm() {
        return "classrooms/form";
    }

    @PostMapping
    public String create(@RequestParam String building,
                         @RequestParam String roomNumber,
                         @RequestParam Integer capacity,
                         RedirectAttributes ra) {
        try {
            classroomService.create(new ClassroomCreateRequest(building.trim(), roomNumber.trim(), capacity));
            ra.addFlashAttribute("success", "Classroom added successfully.");
            return "redirect:/web/classrooms";
        } catch (BadRequestException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/web/classrooms/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("classroom", classroomService.findById(id));
        model.addAttribute("isEdit", true);
        return "classrooms/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String building,
                         @RequestParam String roomNumber,
                         @RequestParam Integer capacity,
                         RedirectAttributes ra) {
        try {
            classroomService.update(id, new ClassroomUpdateRequest(building.trim(), roomNumber.trim(), capacity));
            ra.addFlashAttribute("success", "Classroom updated successfully.");
            return "redirect:/web/classrooms";
        } catch (BadRequestException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/web/classrooms/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            classroomService.delete(id);
            ra.addFlashAttribute("success", "Classroom deleted successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Could not delete classroom: " + e.getMessage());
        }
        return "redirect:/web/classrooms";
    }
}