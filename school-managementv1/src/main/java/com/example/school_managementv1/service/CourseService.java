package com.example.school_managementv1.service;

import com.example.school_managementv1.dto.CourseCreateRequest;
import com.example.school_managementv1.dto.CourseUpdateRequest;
import com.example.school_managementv1.entity.Classroom;
import com.example.school_managementv1.entity.Course;
import com.example.school_managementv1.entity.Teacher;
import com.example.school_managementv1.exception.BadRequestException;
import com.example.school_managementv1.exception.NotFoundException;
import com.example.school_managementv1.repository.ClassroomRepository;
import com.example.school_managementv1.repository.CourseRepository;
import com.example.school_managementv1.repository.TeacherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    private static final Logger log = LoggerFactory.getLogger(CourseService.class);

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final ClassroomRepository classroomRepository;

    public CourseService(CourseRepository courseRepository,
                         TeacherRepository teacherRepository,
                         ClassroomRepository classroomRepository) {
        this.courseRepository = courseRepository;
        this.teacherRepository = teacherRepository;
        this.classroomRepository = classroomRepository;
    }

    public Course create(CourseCreateRequest req) {
        log.debug("Creating course '{}', teacherId={}, classroomId={}", req.name(), req.teacherId(), req.classroomId());
        Teacher teacher = teacherRepository.findById(req.teacherId())
                .orElseThrow(() -> {
                    log.error("Teacher not found: id={}", req.teacherId());
                    return new NotFoundException("Teacher not found: " + req.teacherId());
                });

        Classroom classroom = classroomRepository.findById(req.classroomId())
                .orElseThrow(() -> {
                    log.error("Classroom not found: id={}", req.classroomId());
                    return new NotFoundException("Classroom not found: " + req.classroomId());
                });

        Course c = new Course();
        c.setName(req.name());
        c.setCredits(req.credits());
        c.setTeacher(teacher);
        c.setClassroom(classroom);

        Course saved = courseRepository.save(c);
        log.info("Course created: id={}, name='{}'", saved.getId(), saved.getName());
        return saved;
    }

    public List<Course> findAll() {
        log.debug("Fetching all courses");
        return courseRepository.findAll();
    }

    public Page<Course> findAllPaged(Pageable pageable) {
        log.debug("Fetching courses page={}, size={}, sort={}", pageable.getPageNumber(), pageable.getPageSize(), pageable.getSort());
        return courseRepository.findAll(pageable);
    }

    public Course findById(Long id) {
        log.debug("Fetching course id={}", id);
        return courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Course not found: id={}", id);
                    return new NotFoundException("Course not found: " + id);
                });
    }

    public Course update(Long id, CourseUpdateRequest req) {
        log.debug("Updating course id={}", id);
        Course existing = findById(id);

        Teacher teacher = teacherRepository.findById(req.teacherId())
                .orElseThrow(() -> {
                    log.error("Teacher not found: id={}", req.teacherId());
                    return new NotFoundException("Teacher not found: " + req.teacherId());
                });

        Classroom classroom = classroomRepository.findById(req.classroomId())
                .orElseThrow(() -> {
                    log.error("Classroom not found: id={}", req.classroomId());
                    return new NotFoundException("Classroom not found: " + req.classroomId());
                });

        existing.setName(req.name());
        existing.setCredits(req.credits());
        existing.setTeacher(teacher);
        existing.setClassroom(classroom);

        Course saved = courseRepository.save(existing);
        log.info("Course updated: id={}", saved.getId());
        return saved;
    }

    public void delete(Long id) {
        log.debug("Deleting course id={}", id);
        Course c = findById(id);

        if (c.getEnrollments() != null && !c.getEnrollments().isEmpty()) {
            log.warn("Course delete failed — has active enrollments: id={}", id);
            throw new BadRequestException("Cannot delete course with active enrollments.");
        }

        courseRepository.delete(c);
        log.info("Course deleted: id={}", id);
    }
}
