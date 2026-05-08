package com.example.school_managementv1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(
        name = "attendance",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id", "date"})
)
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne(optional = false)
    @JoinColumn(name = "course_id")
    private Course course;

    @NotNull
    private LocalDate date;

    @NotNull
    private Boolean present;

    public Attendance() {
    }

    public Long getId() {
        return id;
    }

    public Student getStudent() {
        return student;
    }

    public Course getCourse() {
        return course;
    }

    public LocalDate getDate() {
        return date;
    }

    public Boolean getPresent() {
        return present;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setPresent(Boolean present) {
        this.present = present;
    }
}
