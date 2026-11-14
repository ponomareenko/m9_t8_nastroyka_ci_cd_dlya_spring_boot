package ru.netology.dto.request;

import jakarta.validation.constraints.NotNull;

public record UpdateTaskRequest(@NotNull boolean status) {
}
