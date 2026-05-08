package com.example.school_managementv1.controller;

import com.example.school_managementv1.dto.SubjectCreateRequest;
import com.example.school_managementv1.dto.SubjectUpdateRequest;
import com.example.school_managementv1.exception.BadRequestException;
import com.example.school_managementv1.service.SubjectService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/web/subjects")
public class SubjectWebController {

    private final SubjectService subjectService;

    public SubjectWebController(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @GetMapping
    public String list(@RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "10") int size,
                       @RequestParam(defaultValue = "name") String sort,
                       @RequestParam(defaultValue = "asc") String dir,
                       Model model) {
        Sort sorting = dir.equals("desc") ? Sort.by(sort).descending() : Sort.by(sort).ascending();
        model.addAttribute("page", subjectService.findAllPaged(PageRequest.of(page, size, sorting)));
        model.addAttribute("sort", sort);
        model.addAttribute("dir", dir);
        model.addAttribute("size", size);
        return "subjects/list";
    }

    @GetMapping("/new")
    public String newForm() {
        return "subjects/form";
    }

    @PostMapping
    public String create(@RequestParam String name,
                         @RequestParam(required = false) String description,
                         RedirectAttributes ra) {
        try {
            subjectService.create(new SubjectCreateRequest(name.trim(), description != null ? description.trim() : null));
            ra.addFlashAttribute("success", "Subject added successfully.");
            return "redirect:/web/subjects";
        } catch (BadRequestException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/web/subjects/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("subject", subjectService.findById(id));
        model.addAttribute("isEdit", true);
        return "subjects/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam(required = false) String description,
                         RedirectAttributes ra) {
        try {
            subjectService.update(id, new SubjectUpdateRequest(name.trim(), description != null ? description.trim() : null));
            ra.addFlashAttribute("success", "Subject updated successfully.");
            return "redirect:/web/subjects";
        } catch (BadRequestException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/web/subjects/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes ra) {
        try {
            subjectService.delete(id);
            ra.addFlashAttribute("success", "Subject deleted successfully.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Could not delete subject: " + e.getMessage());
        }
        return "redirect:/web/subjects";
    }
}
