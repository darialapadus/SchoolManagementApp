package com.example.school_managementv1.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ClassroomUpdateRequest(
        @NotBlank @Size(min = 1, max = 50) String building,
        @NotBlank @Size(min = 1, max = 20) String roomNumber,
        @NotNull @Min(1) Integer capacity
) {}
