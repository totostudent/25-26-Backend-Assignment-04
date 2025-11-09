package com.gdg.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse { //에러 발생 시 데이터 전달할 객체
    private String code;
    private String message;
}
