package com.example.school_managementv1.service;

import com.example.school_managementv1.dto.ClassroomCreateRequest;
import com.example.school_managementv1.dto.ClassroomUpdateRequest;
import com.example.school_managementv1.entity.Classroom;
import com.example.school_managementv1.exception.BadRequestException;
import com.example.school_managementv1.exception.NotFoundException;
import com.example.school_managementv1.repository.ClassroomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClassroomService {

    private static final Logger log = LoggerFactory.getLogger(ClassroomService.class);

    private final ClassroomRepository classroomRepository;

    public ClassroomService(ClassroomRepository classroomRepository) {
        this.classroomRepository = classroomRepository;
    }

    public Classroom create(ClassroomCreateRequest req) {
        log.debug("Creating classroom: building={}, room={}", req.building(), req.roomNumber());
        if (classroomRepository.existsByBuildingAndRoomNumber(req.building(), req.roomNumber())) {
            log.warn("Classroom creation failed — already exists: {} {}", req.building(), req.roomNumber());
            throw new BadRequestException("Classroom already exists: " + req.building() + " " + req.roomNumber());
        }

        Classroom c = new Classroom();
        c.setBuilding(req.building());
        c.setRoomNumber(req.roomNumber());
        c.setCapacity(req.capacity());

        Classroom saved = classroomRepository.save(c);
        log.info("Classroom created: id={}, building={}, room={}", saved.getId(), saved.getBuilding(), saved.getRoomNumber());
        return saved;
    }

    public List<Classroom> findAll() {
        log.debug("Fetching all classrooms");
        return classroomRepository.findAll();
    }

    public Classroom findById(Long id) {
        log.debug("Fetching classroom id={}", id);
        return classroomRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Classroom not found: id={}", id);
                    return new NotFoundException("Classroom not found: " + id);
                });
    }

    public Classroom update(Long id, ClassroomUpdateRequest req) {
        log.debug("Updating classroom id={}", id);
        Classroom existing = findById(id);

        boolean changedKey =
                !existing.getBuilding().equalsIgnoreCase(req.building())
                        || !existing.getRoomNumber().equalsIgnoreCase(req.roomNumber());

        if (changedKey && classroomRepository.existsByBuildingAndRoomNumber(req.building(), req.roomNumber())) {
            log.warn("Classroom update failed — already exists: {} {}", req.building(), req.roomNumber());
            throw new BadRequestException("Classroom already exists: " + req.building() + " " + req.roomNumber());
        }

        existing.setBuilding(req.building());
        existing.setRoomNumber(req.roomNumber());
        existing.setCapacity(req.capacity());

        Classroom saved = classroomRepository.save(existing);
        log.info("Classroom updated: id={}", saved.getId());
        return saved;
    }

    public void delete(Long id) {
        log.debug("Deleting classroom id={}", id);
        Classroom c = findById(id);

        if (c.getCourses() != null && !c.getCourses().isEmpty()) {
            log.warn("Classroom delete failed — has assigned courses: id={}", id);
            throw new BadRequestException("Cannot delete classroom with assigned courses.");
        }

        classroomRepository.delete(c);
        log.info("Classroom deleted: id={}", id);
    }
}
