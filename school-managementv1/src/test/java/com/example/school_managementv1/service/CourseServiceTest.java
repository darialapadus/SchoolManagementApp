package com.example.school_managementv1.service;

import com.example.school_managementv1.dto.CourseCreateRequest;
import com.example.school_managementv1.entity.Classroom;
import com.example.school_managementv1.entity.Course;
import com.example.school_managementv1.entity.Teacher;
import com.example.school_managementv1.exception.NotFoundException;
import com.example.school_managementv1.repository.ClassroomRepository;
import com.example.school_managementv1.repository.CourseRepository;
import com.example.school_managementv1.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock CourseRepository courseRepository;
    @Mock TeacherRepository teacherRepository;
    @Mock ClassroomRepository classroomRepository;
    @InjectMocks CourseService courseService;

    @Test
    void create_teacherNotFound_throws() {
        CourseCreateRequest req = new CourseCreateRequest("Math", 5, 10L, 20L);
        when(teacherRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> courseService.create(req));
        verify(courseRepository, never()).save(any());
    }

    @Test
    void create_success() {
        CourseCreateRequest req = new CourseCreateRequest("Math", 5, 1L, 2L);

        Teacher t = new Teacher(); t.setId(1L);
        Classroom cr = new Classroom(); cr.setId(2L);

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(t));
        when(classroomRepository.findById(2L)).thenReturn(Optional.of(cr));
        when(courseRepository.save(any(Course.class))).thenAnswer(inv -> inv.getArgument(0));

        Course saved = courseService.create(req);

        assertEquals("Math", saved.getName());
        assertEquals(5, saved.getCredits());
        assertEquals(t, saved.getTeacher());
        assertEquals(cr, saved.getClassroom());
    }
}
