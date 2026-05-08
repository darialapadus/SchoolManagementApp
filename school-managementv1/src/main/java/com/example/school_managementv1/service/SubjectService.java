package com.example.school_managementv1.service;

import com.example.school_managementv1.dto.SubjectCreateRequest;
import com.example.school_managementv1.dto.SubjectUpdateRequest;
import com.example.school_managementv1.entity.Subject;
import com.example.school_managementv1.entity.Teacher;
import com.example.school_managementv1.exception.BadRequestException;
import com.example.school_managementv1.exception.NotFoundException;
import com.example.school_managementv1.repository.SubjectRepository;
import com.example.school_managementv1.repository.TeacherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {

    private static final Logger log = LoggerFactory.getLogger(SubjectService.class);

    private final SubjectRepository subjectRepository;
    private final TeacherRepository teacherRepository;

    public SubjectService(SubjectRepository subjectRepository,
                          TeacherRepository teacherRepository) {
        this.subjectRepository = subjectRepository;
        this.teacherRepository = teacherRepository;
    }

    public Subject create(SubjectCreateRequest req) {
        log.debug("Creating subject: name='{}'", req.name());
        if (subjectRepository.existsByName(req.name())) {
            log.warn("Subject creation failed — name already exists: '{}'", req.name());
            throw new BadRequestException("Subject already exists: " + req.name());
        }

        Subject s = new Subject();
        s.setName(req.name());
        s.setDescription(req.description());

        Subject saved = subjectRepository.save(s);
        log.info("Subject created: id={}, name='{}'", saved.getId(), saved.getName());
        return saved;
    }

    public List<Subject> findAll() {
        log.debug("Fetching all subjects");
        return subjectRepository.findAll();
    }

    public Page<Subject> findAllPaged(Pageable pageable) {
        log.debug("Fetching subjects page={}, size={}, sort={}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return subjectRepository.findAll(pageable);
    }

    public Subject findById(Long id) {
        log.debug("Fetching subject id={}", id);
        return subjectRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Subject not found: id={}", id);
                    return new NotFoundException("Subject not found: " + id);
                });
    }

    public Subject update(Long id, SubjectUpdateRequest req) {
        log.debug("Updating subject id={}", id);
        Subject existing = findById(id);

        if (!existing.getName().equalsIgnoreCase(req.name())
                && subjectRepository.existsByName(req.name())) {
            log.warn("Subject update failed — name already exists: '{}'", req.name());
            throw new BadRequestException("Subject already exists: " + req.name());
        }

        existing.setName(req.name());
        existing.setDescription(req.description());

        Subject saved = subjectRepository.save(existing);
        log.info("Subject updated: id={}", saved.getId());
        return saved;
    }

    public void delete(Long id) {
        log.debug("Deleting subject id={}", id);
        Subject s = findById(id);
        subjectRepository.delete(s);
        log.info("Subject deleted: id={}", id);
    }

    public Teacher assignSubjectToTeacher(Long subjectId, Long teacherId) {
        log.debug("Assigning subject id={} to teacher id={}", subjectId, teacherId);
        Subject subject = findById(subjectId);
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> {
                    log.error("Teacher not found: id={}", teacherId);
                    return new NotFoundException("Teacher not found: " + teacherId);
                });

        if (!teacher.getSubjects().contains(subject)) {
            teacher.getSubjects().add(subject);
            teacherRepository.save(teacher);
            log.info("Subject id={} assigned to teacher id={}", subjectId, teacherId);
        }
        return teacher;
    }

    public Teacher removeSubjectFromTeacher(Long subjectId, Long teacherId) {
        log.debug("Removing subject id={} from teacher id={}", subjectId, teacherId);
        Subject subject = findById(subjectId);
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> {
                    log.error("Teacher not found: id={}", teacherId);
                    return new NotFoundException("Teacher not found: " + teacherId);
                });

        teacher.getSubjects().remove(subject);
        teacherRepository.save(teacher);
        log.info("Subject id={} removed from teacher id={}", subjectId, teacherId);
        return teacher;
    }
}
