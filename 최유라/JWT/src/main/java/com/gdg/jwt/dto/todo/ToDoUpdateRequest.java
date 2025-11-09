package com.gdg.jwt.dto.todo;

public record ToDoUpdateRequest(
        String work,
        String daysLeft
) {
}
