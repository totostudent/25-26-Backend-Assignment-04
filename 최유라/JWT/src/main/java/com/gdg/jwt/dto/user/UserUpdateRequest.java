package com.gdg.jwt.dto.user;

public record UserUpdateRequest(
        String password,
        String name
) {
}
