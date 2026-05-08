package com.example.school_managementv1.service;

import com.example.school_managementv1.dto.TeacherCreateRequest;
import com.example.school_managementv1.dto.TeacherUpdateRequest;
import com.example.school_managementv1.entity.Teacher;
import com.example.school_managementv1.exception.BadRequestException;
import com.example.school_managementv1.exception.NotFoundException;
import com.example.school_managementv1.repository.TeacherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {

    private static final Logger log = LoggerFactory.getLogger(TeacherService.class);

    private final TeacherRepository teacherRepository;

    public TeacherService(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    public Teacher create(TeacherCreateRequest req) {
        log.debug("Creating teacher with email: {}", req.email());
        if (teacherRepository.existsByEmail(req.email())) {
            log.warn("Teacher creation failed — email already exists: {}", req.email());
            throw new BadRequestException("Teacher email already exists: " + req.email());
        }

        Teacher t = new Teacher();
        t.setFirstName(req.firstName());
        t.setLastName(req.lastName());
        t.setEmail(req.email());
        t.setDepartment(req.department());

        Teacher saved = teacherRepository.save(t);
        log.info("Teacher created: id={}, email={}", saved.getId(), saved.getEmail());
        return saved;
    }

    public List<Teacher> findAll() {
        log.debug("Fetching all teachers");
        return teacherRepository.findAll();
    }

    public Page<Teacher> findAllPaged(Pageable pageable) {
        log.debug("Fetching teachers page={}, size={}, sort={}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return teacherRepository.findAll(pageable);
    }

    public Teacher findById(Long id) {
        log.debug("Fetching teacher id={}", id);
        return teacherRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Teacher not found: id={}", id);
                    return new NotFoundException("Teacher not found: " + id);
                });
    }

    public Teacher update(Long id, TeacherUpdateRequest req) {
        log.debug("Updating teacher id={}", id);
        Teacher existing = findById(id);

        if (!existing.getEmail().equalsIgnoreCase(req.email())
                && teacherRepository.existsByEmail(req.email())) {
            log.warn("Teacher update failed — email already exists: {}", req.email());
            throw new BadRequestException("Teacher email already exists: " + req.email());
        }

        existing.setFirstName(req.firstName());
        existing.setLastName(req.lastName());
        existing.setEmail(req.email());
        existing.setDepartment(req.department());

        Teacher saved = teacherRepository.save(existing);
        log.info("Teacher updated: id={}", saved.getId());
        return saved;
    }

    public void delete(Long id) {
        log.debug("Deleting teacher id={}", id);
        Teacher t = findById(id);

        if (t.getCourses() != null && !t.getCourses().isEmpty()) {
            log.warn("Teacher delete failed — has assigned courses: id={}", id);
            throw new BadRequestException("Cannot delete teacher with assigned courses.");
        }

        teacherRepository.delete(t);
        log.info("Teacher deleted: id={}", id);
    }
}
