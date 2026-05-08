package com.example.school_managementv1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @NotNull
    private Integer credits;

    @ManyToOne(optional = false)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(optional = false)
    @JoinColumn(name = "classroom_id")
    private Classroom classroom;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Enrollment> enrollments = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Attendance> attendance = new ArrayList<>();

    public Course() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getCredits() {
        return credits;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public List<Attendance> getAttendance() {
        return attendance;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public void setAttendance(List<Attendance> attendance) {
        this.attendance = attendance;
    }
}
