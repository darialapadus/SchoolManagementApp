package com.example.school_managementv1.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record GradeRequest(
        @NotNull Long enrollmentId,
        @NotNull @Min(1) @Max(10) Integer value
) {}
