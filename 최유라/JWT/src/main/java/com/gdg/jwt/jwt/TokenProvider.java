package com.gdg.jwt.jwt;

import com.gdg.jwt.domain.User;
import com.gdg.jwt.exception.CustomException;
import com.gdg.jwt.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider {

    private static final String ROLE_CLAIM = "Role";
    private static final String BEARER = "Bearer ";
    private static final String AUTHORIZATION = "Authorization";

    private final SecretKey key;
    private final long accessTokenValidityTime; //토큰 만료 시간

    public TokenProvider(@Value("${jwt.secret}") String secretKey,
                         @Value("${jwt.access-token-validity-in-milliseconds}") long accessTokenValidityTime) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); //64진수로 인코딩된 secretKey를 디코딩해 원래 바이트 배열 키로 변환
        this.key = Keys.hmacShaKeyFor(keyBytes); // 내부적으로 HS256 알고리즘 포함
        this.accessTokenValidityTime = accessTokenValidityTime;
    }

    public String createAccessToken(User user) {
        long nowTime = (new Date().getTime());
        Date accessTokenExpiredTime = new Date(nowTime + accessTokenValidityTime);

        return Jwts.builder()
                .subject(user.getId().toString()) // 토큰 제목(사용자 이름)
                .claim(ROLE_CLAIM, user.getRole().name()) // 권한 정보
                .expiration(accessTokenExpiredTime) // 토큰 만료 시간
                .signWith(key) // 지정된 키로 서명
                .compact(); // 최종 JWT 문자열 생성
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (claims.get(ROLE_CLAIM) == null) { //토큰의 claims 맵 내에 있는 권한 정보가 없을 때
            throw new CustomException(ErrorCode.NO_ROLE_TOKEN);
        }

        // 사용자의 권한 정보를 securityContextHolder에 담아준다
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(ROLE_CLAIM).toString().split(","))
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                        .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
        authentication.setDetails(claims);

        return authentication;
    }

    public String resolveToken(HttpServletRequest request) { // 토큰 분해/분석
        String bearerToken = request.getHeader(AUTHORIZATION);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER)) { //null, 길이가 0, 공백이 아니고 'BEARER'로 시작하는 토큰일 때
            return bearerToken.substring(BEARER.length()); // "Bearer " 접두사 제거하여 순수 토큰만 반환(bearerToken에서 bearer 문자열 길이만큼 앞에서부터 지워나감)
        }
        return null; //위 조건 만족 못하는 경우
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException |
                 ExpiredJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) { //유효 기간 지난 JWT토큰 수신받았을 때
            return e.getClaims(); //예외 발생한 객체의 클레임(사용자 정보) 반환
        } catch (SecurityException e) { //토큰 변조됨, 복호화를 시도한 키가 올바르지 않음
            throw new CustomException(ErrorCode.CAN_NOT_USE_TOKEN);
        }
    }
}
