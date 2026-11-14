package ru.netology.dto.response;

import java.util.List;

public record AllTasksResponse(List<TaskResponse> tasks) {
}