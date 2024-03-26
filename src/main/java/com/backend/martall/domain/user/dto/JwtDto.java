package com.backend.martall.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

public class JwtDto {
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class JwtDateDto {
        @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWR4IjoxLCJpYXQiOjE3MTEzNjI0MzMsImV4cCI6MTcxMTM2NDIzM30.kwVLt_59zbD8XoC4Q2yjT2YyBZ5Ajx-l1I--qAe0SCg")
        private String token;
        private Date expiredDate;

    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class TwoJwtDateDto {
        @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJ1c2VySWR4IjoxLCJpYXQiOjE3MTEzNjI0MzMsImV4cCI6MTcxMTM2NDIzM30.kwVLt_59zbD8XoC4Q2yjT2YyBZ5Ajx-l1I--qAe0SCg")
        private String access_token;
        private Date access_expiredDate;
        @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE3MTEzNjI0MzMsImV4cCI6MTcxMTk2NzIzM30.5EO8KRAzbqucMLzUzjZPC_H2Ym-dizxADpLgQgkD7Vw")
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
