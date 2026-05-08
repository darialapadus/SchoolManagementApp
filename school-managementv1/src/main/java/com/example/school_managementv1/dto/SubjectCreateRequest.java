package com.example.school_managementv1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SubjectCreateRequest(
        @NotBlank @Size(min = 2, max = 100) String name,
        @Size(max = 255) String description
) {}
