package com.example.school_managementv1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CourseCreateRequest(
        @NotBlank @Size(min = 2, max = 100) String name,
        @NotNull Integer credits,
        @NotNull Long teacherId,
        @NotNull Long classroomId
) {}
