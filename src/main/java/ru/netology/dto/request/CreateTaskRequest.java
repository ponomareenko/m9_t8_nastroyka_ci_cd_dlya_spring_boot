package ru.netology.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateTaskRequest(@NotBlank String title) {
}
