package com.example.school_managementv1.service;

import com.example.school_managementv1.dto.GradeRequest;
import com.example.school_managementv1.entity.Enrollment;
import com.example.school_managementv1.entity.Grade;
import com.example.school_managementv1.exception.NotFoundException;
import com.example.school_managementv1.repository.EnrollmentRepository;
import com.example.school_managementv1.repository.GradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GradeService {

    private static final Logger log = LoggerFactory.getLogger(GradeService.class);

    private final GradeRepository gradeRepository;
    private final EnrollmentRepository enrollmentRepository;

    public GradeService(GradeRepository gradeRepository,
                        EnrollmentRepository enrollmentRepository) {
        this.gradeRepository = gradeRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    public Grade setGrade(GradeRequest req) {
        log.debug("Setting grade for enrollmentId={}, value={}", req.enrollmentId(), req.value());
        Enrollment enrollment = enrollmentRepository.findById(req.enrollmentId())
                .orElseThrow(() -> {
                    log.error("Enrollment not found: id={}", req.enrollmentId());
                    return new NotFoundException("Enrollment not found: " + req.enrollmentId());
                });

        boolean isUpdate = gradeRepository.existsByEnrollmentId(req.enrollmentId());
        Grade grade = gradeRepository.findByEnrollmentId(req.enrollmentId())
                .orElseGet(Grade::new);

        grade.setEnrollment(enrollment);
        grade.setValue(req.value());
        grade.setGradedAt(LocalDate.now());

        Grade saved = gradeRepository.save(grade);
        log.info("{} grade: id={}, enrollmentId={}, value={}",
                isUpdate ? "Updated" : "Created", saved.getId(), req.enrollmentId(), req.value());
        return saved;
    }

    public List<Grade> findAll() {
        log.debug("Fetching all grades");
        return gradeRepository.findAll();
    }

    public Grade findById(Long id) {
        log.debug("Fetching grade id={}", id);
        return gradeRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Grade not found: id={}", id);
                    return new NotFoundException("Grade not found: " + id);
                });
    }

    public void delete(Long id) {
        log.debug("Deleting grade id={}", id);
        Grade g = findById(id);
        gradeRepository.delete(g);
        log.info("Grade deleted: id={}", id);
    }
}
