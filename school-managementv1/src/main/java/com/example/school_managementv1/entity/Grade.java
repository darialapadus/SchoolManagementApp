package com.example.school_managementv1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "grades")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(optional = false)
    @JoinColumn(name = "enrollment_id", unique = true)
    private Enrollment enrollment;

    @Min(1)
    @Max(10)
    private Integer value;

    @NotNull
    private LocalDate gradedAt;

    public Grade() {
    }

    public Long getId() {
        return id;
    }

    public Enrollment getEnrollment() {
        return enrollment;
    }

    public Integer getValue() {
        return value;
    }

    public LocalDate getGradedAt() {
        return gradedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEnrollment(Enrollment enrollment) {
        this.enrollment = enrollment;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void setGradedAt(LocalDate gradedAt) {
        this.gradedAt = gradedAt;
    }
}
