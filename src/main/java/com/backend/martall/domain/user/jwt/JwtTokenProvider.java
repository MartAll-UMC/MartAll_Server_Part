package com.backend.martall.domain.user.jwt;

import com.backend.martall.domain.user.entity.UserRepository;
import com.backend.martall.global.exception.GlobalException;
import com.backend.martall.global.exception.ResponseStatus;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
    private final Key key;
    private final UserRepository userRepository;
    public JwtTokenProvider(@Value("${jwt.secret.key}") String secretKey, UserRepository userRepository) {
        this.userRepository = userRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Long userIdx) {
        return createToken(userIdx);
    }

    private String createToken(Long userIdx) {
        Claims claims = Jwts.claims();
        claims.put("userIdx", userIdx); //실제 jwt에는 사용자의 idx

        Date now = new Date();
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + (30 * 60 * 1000L))) //30분
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

//    public Authentication getAuthentication(String token) {
//        Claims claims = Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//
//
//    }

    //토큰에서 회원 정보 추출
    public Long getUserIdx(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.get("userIdx", Long.class);
    }

    //토큰의 유효성과 만료일자 확인
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (SecurityException e) {
            log.info("Invalid JWT signature.");
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT token.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token. at validation Token");
            throw new GlobalException(ResponseStatus.LOGIN_FAIL_EXPIRED_JWT);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token.");
        } catch (IllegalArgumentException e) {
            log.info("JWT token compact of handler are invalid.");
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    //Request의 Header에서 token값 가져오기
    private String getJwt() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String token = request.getHeader("access-token");

        if (token == null || token.isEmpty()) {
            throw new GlobalException(ResponseStatus.FAIL_ACCESS_EMPRY_JWT);
        }

        return token;
    }

    //올바른 Token인지 검증
    public Long resolveToken() {
        String token = getJwt();
        if(!validateToken(token)){
            throw new GlobalException(ResponseStatus.LOGIN_FAIL_WRONG_JWT);
        }

        Long user_id = getUserIdx(token);

        userRepository.findByUserIdx(user_id)
                .orElseThrow(()->new GlobalException(ResponseStatus.LOGIN_FAIL_WRONG_JWT));

        return user_id;
    }

    //https://leeeehhjj.tistory.com/61
}