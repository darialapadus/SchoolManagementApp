package com.example.school_managementv1.controller;

import com.example.school_managementv1.dto.CourseCreateRequest;
import com.example.school_managementv1.dto.CourseUpdateRequest;
import com.example.school_managementv1.exception.BadRequestException;
import com.example.school_managementv1.service.ClassroomService;
import com.example.school_managementv1.service.CourseService;
import com.example.school_managementv1.service.TeacherService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/courses")
public class CourseWebController {

    private final CourseService courseService;
    private final TeacherService teacherService;
    private final ClassroomService classroomService;

    public CourseWebController(CourseService courseService,
                               TeacherService teacherService,
                               ClassroomService classroomService) {
        this.courseService = courseService;
        this.teacherService = teacherService;
        this.classroomService = classroomService;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "name") String sort,
                       @RequestParam(defaultValue = "asc") String dir,
                       Model model) {
        Sort sorting = dir.equals("desc") ? Sort.by(sort).descending() : Sort.by(sort).ascending();
        model.addAttribute("page", courseService.findAllPaged(PageRequest.of(page, size, sorting)));
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("size", size);
        return "courses/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("classrooms", classroomService.findAll());
        return "courses/form";
    }

    @PostMapping
    public String create(@RequestParam String name,
                         @RequestParam Integer credits,
                         @RequestParam Long teacherId,
                         @RequestParam Long classroomId,
                         RedirectAttributes ra) {
        try {
            courseService.create(new CourseCreateRequest(name.trim(), credits, teacherId, classroomId));
            ra.addFlashAttribute("success", "Course added successfully.");
            return "redirect:/web/courses";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/web/courses/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("course", courseService.findById(id));
        model.addAttribute("teachers", teacherService.findAll());
        model.addAttribute("classrooms", classroomService.findAll());
        model.addAttribute("isEdit", true);
        return "courses/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam Integer credits,
                         @RequestParam Long teacherId,
                         @RequestParam Long classroomId,
                         RedirectAttributes ra) {
        try {
            courseService.update(id, new CourseUpdateRequest(name.trim(), credits, teacherId, classroomId));
            ra.addFlashAttribute("success", "Course updated successfully.");
            return "redirect:/web/courses";
        } catch (Exception e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/web/courses/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            courseService.delete(id);
            ra.addFlashAttribute("success", "Course deleted successfully.");
        } catch (BadRequestException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/web/courses";
    }
}
