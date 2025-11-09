package com.gdg.jwt.dto.user;

import com.gdg.jwt.domain.User;
import lombok.Builder;

@Builder
public record UserInfoResponse(
        Long id,
        String email,
        String name,
        String role
) {
    public static UserInfoResponse fromEntity(User user) {
        return UserInfoResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().toString())
                .build();
    }
}
