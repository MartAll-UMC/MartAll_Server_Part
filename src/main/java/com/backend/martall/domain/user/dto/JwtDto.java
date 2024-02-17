package com.backend.martall.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

public class JwtDto {
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class JwtDateDto {
        private String token;
        private Date expiredDate;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class TwoJwtDateDto {
        private String access_token;
        private Date access_expiredDate;
        private String refresh_token;
        private Date refresh_expiredDate;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class RefreshJwtDto {
        private Long id;
        private String refresh_token;
    }
}
