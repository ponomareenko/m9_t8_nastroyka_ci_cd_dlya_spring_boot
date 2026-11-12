package ru.netology.dto;

import ru.netology.model.Task;

public record AllTasksResponse(Task[] tasks) {
}
