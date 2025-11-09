package com.gdg.jwt.dto.user;

public record UserSignUpRequest(
        String email,
        String password,
        String name
) {
}
