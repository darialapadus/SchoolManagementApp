package com.example.school_managementv1.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(
        name = "enrollments",
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "course_id"})
)
public class Enrollment {

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
    private LocalDate enrolledAt;

    @OneToOne(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Grade grade;

    public Enrollment() {
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

    public LocalDate getEnrolledAt() {
        return enrolledAt;
    }

    public Grade getGrade() {
        return grade;
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

    public void setEnrolledAt(LocalDate enrolledAt) {
        this.enrolledAt = enrolledAt;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }
}
