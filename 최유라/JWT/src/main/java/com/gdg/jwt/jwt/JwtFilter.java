package com.gdg.jwt.jwt;

import com.gdg.jwt.exception.CustomException;
import com.gdg.jwt.exception.ErrorCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final com.gdg.jwt.jwt.TokenProvider tokenProvider;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String token = tokenProvider.resolveToken(httpRequest);

        if (StringUtils.hasText(token)) { //token 문자열이 null, 공백이 아님
            if (tokenProvider.validateToken(token)) { //문자열(token)이 유효한지 검사
                Authentication authentication = tokenProvider.getAuthentication(token); //payload에서 사용자 인증 정보 가져옴
                SecurityContextHolder.getContext().setAuthentication(authentication); //사용자 인증 정보를 securityContext에 담음
            } else {
                throw new CustomException(ErrorCode.NO_LONGER_TOKEN);
            }
        }

        filterChain.doFilter(request, response);
    }
}
