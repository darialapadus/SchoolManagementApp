package com.example.school_managementv1.service;

import com.example.school_managementv1.dto.StudentCreateRequest;
import com.example.school_managementv1.dto.StudentUpdateRequest;
import com.example.school_managementv1.entity.Student;
import com.example.school_managementv1.exception.BadRequestException;
import com.example.school_managementv1.exception.NotFoundException;
import com.example.school_managementv1.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private static final Logger log = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student create(StudentCreateRequest req) {
        log.debug("Creating student with email: {}", req.email());
        if (studentRepository.existsByEmail(req.email())) {
            log.warn("Student creation failed — email already exists: {}", req.email());
            throw new BadRequestException("Student email already exists: " + req.email());
        }

        Student s = new Student();
        s.setFirstName(req.firstName());
        s.setLastName(req.lastName());
        s.setEmail(req.email());
        s.setGradeLevel(req.gradeLevel());

        Student saved = studentRepository.save(s);
        log.info("Student created with id={}, email={}", saved.getId(), saved.getEmail());
        return saved;
    }

    public List<Student> findAll() {
        log.debug("Fetching all students");
        return studentRepository.findAll();
    }

    public Page<Student> findAllPaged(Pageable pageable) {
        log.debug("Fetching students page={}, size={}, sort={}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return studentRepository.findAll(pageable);
    }

    public Student findById(Long id) {
        log.debug("Fetching student id={}", id);
        return studentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Student not found: id={}", id);
                    return new NotFoundException("Student not found: " + id);
                });
    }

    public Student update(Long id, StudentUpdateRequest req) {
        log.debug("Updating student id={}", id);
        Student existing = findById(id);

        if (!existing.getEmail().equalsIgnoreCase(req.email())
                && studentRepository.existsByEmail(req.email())) {
            log.warn("Student update failed — email already exists: {}", req.email());
            throw new BadRequestException("Student email already exists: " + req.email());
        }

        existing.setFirstName(req.firstName());
        existing.setLastName(req.lastName());
        existing.setEmail(req.email());
        existing.setGradeLevel(req.gradeLevel());

        Student saved = studentRepository.save(existing);
        log.info("Student updated: id={}", saved.getId());
        return saved;
    }

    public void delete(Long id) {
        log.debug("Deleting student id={}", id);
        Student s = findById(id);
        studentRepository.delete(s);
        log.info("Student deleted: id={}", id);
    }
}
