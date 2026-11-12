package ru.netology.dto.response;

public record TaskResponse(int id, String title, boolean status, String createdAt) {
}
