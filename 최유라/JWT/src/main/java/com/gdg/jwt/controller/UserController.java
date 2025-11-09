package com.gdg.jwt.controller;

import com.gdg.jwt.dto.jwt.TokenResponse;
import com.gdg.jwt.dto.user.UserInfoResponse;
import com.gdg.jwt.dto.user.UserSignUpRequest;
import com.gdg.jwt.dto.user.UserUpdateRequest;
import com.gdg.jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<TokenResponse> signUp(@RequestBody UserSignUpRequest userSignUpRequest) {
        return ResponseEntity.created(URI.create("/user/")).body(userService.signUp(userSignUpRequest));
    }

    @GetMapping("/my")
    public ResponseEntity<UserInfoResponse> getMyInfo(Principal principal) { //토큰 확인
        return ResponseEntity.ok(userService.getMyInfo(principal));
    }

    @GetMapping("/info/{userId}")
    public ResponseEntity<UserInfoResponse> getUserInfo(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserInfo(userId));
    }

    @PatchMapping("/update")
    public ResponseEntity<UserInfoResponse> updateMyInfo(Principal principal, @RequestBody UserUpdateRequest userUpdateRequest) {
        return ResponseEntity.ok(userService.updateMyInfo(principal, userUpdateRequest));
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteUser(Principal principal) {
        userService.deleteUser(principal);
        return ResponseEntity.noContent().build();
    }
}
