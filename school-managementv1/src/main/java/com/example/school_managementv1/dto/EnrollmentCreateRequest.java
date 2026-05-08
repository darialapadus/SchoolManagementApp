package com.example.school_managementv1.dto;

import jakarta.validation.constraints.NotNull;

public record EnrollmentCreateRequest(
        @NotNull Long studentId,
        @NotNull Long courseId
) {}
