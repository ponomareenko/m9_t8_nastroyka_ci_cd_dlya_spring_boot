package ru.netology.dto.response;

import ru.netology.model.Task;

public record AllTasksResponse(Task[] tasks) {
}
