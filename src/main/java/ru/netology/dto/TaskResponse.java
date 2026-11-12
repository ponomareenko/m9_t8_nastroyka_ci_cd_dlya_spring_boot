package ru.netology.dto;

public record TaskResponse(int id, String title, boolean status, String createdAt) {
}
