package com.example.school_managementv1.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AttendanceRequest(
        @NotNull Long studentId,
        @NotNull Long courseId,
        @NotNull LocalDate date,
        @NotNull Boolean present
) {}
