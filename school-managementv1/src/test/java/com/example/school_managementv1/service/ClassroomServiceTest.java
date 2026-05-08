package com.example.school_managementv1.service;

import com.example.school_managementv1.dto.ClassroomCreateRequest;
import com.example.school_managementv1.dto.ClassroomUpdateRequest;
import com.example.school_managementv1.entity.Classroom;
import com.example.school_managementv1.entity.Course;
import com.example.school_managementv1.exception.BadRequestException;
import com.example.school_managementv1.exception.NotFoundException;
import com.example.school_managementv1.repository.ClassroomRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClassroomServiceTest {

    @Mock ClassroomRepository classroomRepository;
    @InjectMocks ClassroomService classroomService;

    @Test
    void create_duplicate_throws() {
        ClassroomCreateRequest req = new ClassroomCreateRequest("A", "101", 30);
        when(classroomRepository.existsByBuildingAndRoomNumber("A", "101")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> classroomService.create(req));
    }

    @Test
    void findById_notFound_throws() {
        when(classroomRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> classroomService.findById(1L));
    }

    @Test
    void delete_withCourses_throws() {
        Classroom c = new Classroom();
        c.setId(1L);
        c.getCourses().add(new Course());
        when(classroomRepository.findById(1L)).thenReturn(Optional.of(c));

        assertThrows(BadRequestException.class, () -> classroomService.delete(1L));
        verify(classroomRepository, never()).delete(any());
    }
}
