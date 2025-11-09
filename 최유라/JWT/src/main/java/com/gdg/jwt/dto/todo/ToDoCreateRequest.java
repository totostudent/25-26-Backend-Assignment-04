package com.gdg.jwt.dto.todo;

public record ToDoCreateRequest(
        String work,
        String daysLeft
) {
}
