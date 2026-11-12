package ru.netology.dto;

public record TaskResponse(int id, String title, String status, String createdAt) {
}
