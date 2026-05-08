package com.example.school_managementv1.service;

import com.example.school_managementv1.dto.EnrollmentCreateRequest;
import com.example.school_managementv1.entity.Course;
import com.example.school_managementv1.entity.Enrollment;
import com.example.school_managementv1.entity.Student;
import com.example.school_managementv1.exception.BadRequestException;
import com.example.school_managementv1.exception.NotFoundException;
import com.example.school_managementv1.repository.CourseRepository;
import com.example.school_managementv1.repository.EnrollmentRepository;
import com.example.school_managementv1.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EnrollmentService {

    private static final Logger log = LoggerFactory.getLogger(EnrollmentService.class);

    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository,
                             StudentRepository studentRepository,
                             CourseRepository courseRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public Enrollment enroll(EnrollmentCreateRequest req) {
        log.debug("Enrolling studentId={} in courseId={}", req.studentId(), req.courseId());
        if (enrollmentRepository.existsByStudentIdAndCourseId(req.studentId(), req.courseId())) {
            log.warn("Enrollment failed — student {} already enrolled in course {}", req.studentId(), req.courseId());
            throw new BadRequestException("Student already enrolled in this course.");
        }

        Student student = studentRepository.findById(req.studentId())
                .orElseThrow(() -> {
                    log.error("Student not found: id={}", req.studentId());
                    return new NotFoundException("Student not found: " + req.studentId());
                });

        Course course = courseRepository.findById(req.courseId())
                .orElseThrow(() -> {
                    log.error("Course not found: id={}", req.courseId());
                    return new NotFoundException("Course not found: " + req.courseId());
                });

        Enrollment e = new Enrollment();
        e.setStudent(student);
        e.setCourse(course);
        e.setEnrolledAt(LocalDate.now());

        Enrollment saved = enrollmentRepository.save(e);
        log.info("Enrollment created: id={}, studentId={}, courseId={}", saved.getId(), req.studentId(), req.courseId());
        return saved;
    }

    public Enrollment findById(Long id) {
        log.debug("Fetching enrollment id={}", id);
        return enrollmentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Enrollment not found: id={}", id);
                    return new NotFoundException("Enrollment not found: " + id);
                });
    }

    public List<Enrollment> findAll() {
        log.debug("Fetching all enrollments");
        return enrollmentRepository.findAll();
    }

    public List<Enrollment> listByStudent(Long studentId) {
        log.debug("Fetching enrollments for studentId={}", studentId);
        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Enrollment> listByCourse(Long courseId) {
        log.debug("Fetching enrollments for courseId={}", courseId);
        return enrollmentRepository.findByCourseId(courseId);
    }

    public void delete(Long id) {
        log.debug("Deleting enrollment id={}", id);
        Enrollment e = findById(id);
        enrollmentRepository.delete(e);
        log.info("Enrollment deleted: id={}", id);
    }
}
