package com.gdg.jwt.service;

import com.gdg.jwt.domain.Role;
import com.gdg.jwt.domain.User;
import com.gdg.jwt.dto.jwt.TokenResponse;
import com.gdg.jwt.dto.user.UserInfoResponse;
import com.gdg.jwt.dto.user.UserSignUpRequest;
import com.gdg.jwt.dto.user.UserUpdateRequest;
import com.gdg.jwt.exception.CustomException;
import com.gdg.jwt.exception.ErrorCode;
import com.gdg.jwt.jwt.TokenProvider;
import com.gdg.jwt.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public TokenResponse signUp(UserSignUpRequest userSignupRequest) {
        User user = userRepository.save(User.builder()
                .email(userSignupRequest.email())
                .password(passwordEncoder.encode(userSignupRequest.password()))
                .name(userSignupRequest.name())
                .role(Role.ROLE_USER)
                .build());

        return TokenResponse.builder()
                .accessToken(tokenProvider.createAccessToken(user))
                .build();
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getMyInfo(Principal principal) {
        User user = getUserEntity(Long.parseLong(principal.getName()));

        return UserInfoResponse.fromEntity(user);
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(Long userId) {
        User user = getUserEntity(userId);

        return UserInfoResponse.fromEntity(user);
    }

    @Transactional
    public UserInfoResponse updateMyInfo(Principal principal, UserUpdateRequest userUpdateRequest) {
        User user = getUserEntity(Long.parseLong(principal.getName()));
        user.updateInfo(
                userUpdateRequest.password() == null ? user.getPassword() : passwordEncoder.encode(userUpdateRequest.password()),
                userUpdateRequest.name() == null ? user.getName() : userUpdateRequest.name()
        );

        return UserInfoResponse.fromEntity(user);
    }

    @Transactional
    public void deleteUser(Principal principal) {
        userRepository.deleteById(Long.parseLong(principal.getName()));
    }

    public User getUserEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
    }
}
