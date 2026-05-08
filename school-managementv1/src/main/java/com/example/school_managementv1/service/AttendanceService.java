package com.example.school_managementv1.service;

import com.example.school_managementv1.dto.AttendanceRequest;
import com.example.school_managementv1.entity.Attendance;
import com.example.school_managementv1.entity.Course;
import com.example.school_managementv1.entity.Student;
import com.example.school_managementv1.exception.BadRequestException;
import com.example.school_managementv1.exception.NotFoundException;
import com.example.school_managementv1.repository.AttendanceRepository;
import com.example.school_managementv1.repository.CourseRepository;
import com.example.school_managementv1.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceService {

    private static final Logger log = LoggerFactory.getLogger(AttendanceService.class);

    private final AttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public AttendanceService(AttendanceRepository attendanceRepository,
                             StudentRepository studentRepository,
                             CourseRepository courseRepository) {
        this.attendanceRepository = attendanceRepository;
        this.studentRepository = studentRepository;
        this.courseRepository = courseRepository;
    }

    public Attendance mark(AttendanceRequest req) {
        log.debug("Marking attendance: studentId={}, courseId={}, date={}, present={}",
                req.studentId(), req.courseId(), req.date(), req.present());

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

        if (attendanceRepository.existsByStudentIdAndCourseIdAndDate(
                req.studentId(), req.courseId(), req.date())) {
            log.warn("Attendance already recorded: studentId={}, courseId={}, date={}",
                    req.studentId(), req.courseId(), req.date());
            throw new BadRequestException("Attendance already recorded for this student/course/date.");
        }

        Attendance a = new Attendance();
        a.setStudent(student);
        a.setCourse(course);
        a.setDate(req.date());
        a.setPresent(req.present());

        Attendance saved = attendanceRepository.save(a);
        log.info("Attendance marked: id={}, studentId={}, courseId={}, date={}, present={}",
                saved.getId(), req.studentId(), req.courseId(), req.date(), req.present());
        return saved;
    }

    public List<Attendance> findAll() {
        log.debug("Fetching all attendance records");
        return attendanceRepository.findAll();
    }

    public Attendance findById(Long id) {
        log.debug("Fetching attendance id={}", id);
        return attendanceRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Attendance not found: id={}", id);
                    return new NotFoundException("Attendance not found: " + id);
                });
    }

    public List<Attendance> findByStudent(Long studentId) {
        log.debug("Fetching attendance for studentId={}", studentId);
        return attendanceRepository.findByStudentId(studentId);
    }

    public List<Attendance> findByCourse(Long courseId) {
        log.debug("Fetching attendance for courseId={}", courseId);
        return attendanceRepository.findByCourseId(courseId);
    }

    public void delete(Long id) {
        log.debug("Deleting attendance id={}", id);
        Attendance a = findById(id);
        attendanceRepository.delete(a);
        log.info("Attendance deleted: id={}", id);
    }
}
