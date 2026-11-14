package ru.netology.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateTaskRequest(@NotBlank String title, boolean status) {
}
