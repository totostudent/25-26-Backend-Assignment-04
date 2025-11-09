package com.gdg.jwt.dto.todo;

import com.gdg.jwt.domain.ToDo;
import lombok.Builder;

@Builder
public record ToDoInfoResponse(
        Long id,
        String work,
        String daysLeft,
        String author
) {
    public static ToDoInfoResponse fromEntity(ToDo todo) {
        return ToDoInfoResponse.builder()
                .id(todo.getId())
                .work(todo.getWork())
                .daysLeft(todo.getDaysLeft())
                .author(todo.getUser().getName())
                .build();
    }
}
