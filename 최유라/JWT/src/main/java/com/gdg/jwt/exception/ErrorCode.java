package com.gdg.jwt.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode { //에러 코드, 메시지, HTTP 상태 정의

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "user-001", "유저가 존재하지 않습니다."),
    ALREADY_LOGIN(HttpStatus.BAD_REQUEST,"user-002", "이미 로그인된 상태입니다."),
    WORK_NOT_FOUND(HttpStatus.NOT_FOUND, "todo-001", "요청하신 내용의 일정을 찾을 수 없습니다."),
    HAVE_NO_ROLE(HttpStatus.FORBIDDEN, "todo-002", "해당 데이터에 접근할 권한이 없습니다."),
    NO_ROLE_TOKEN(HttpStatus.UNAUTHORIZED, "token-001", "권한 정보가 없는 토큰입니다."),
    CAN_NOT_USE_TOKEN(HttpStatus.UNAUTHORIZED, "token-002", "토큰 복호화에 실패했습니다.(변조되었거나 키가 올바르지 않음)"),
    NO_LONGER_TOKEN(HttpStatus.UNAUTHORIZED, "jwt-001", "Invalid or expired token(만료되었거나 유효하지 않은 토큰입니다)");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
