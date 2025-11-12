package com.gdg.jwt.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException { //도메인별 예외 정의(비즈니스, 인증?)
    ErrorCode errorCode; //errorcode 속성 추가
}
