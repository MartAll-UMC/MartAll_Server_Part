package com.backend.martall.domain.user.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

public class UserDto {

    @NoArgsConstructor
    @Data
    public static class UserRequestDto {
        private String id;
        private String password;
        private String username;
        private String phoneNumber;
        private String email;
        private String provider;
        private Long providerId;
        private Integer userType;
        private String imgUrl;
    }

    @NoArgsConstructor
    @Data
    public static class UserModifyRequestDto {
        //jwt & 이미지 파일
        private String id;
        private String username;
        private String phoneNumber;
        private String imgUrl;
        private String email;
    }

    @NoArgsConstructor
    @Data
    public static class UserInfoResponseDto {
        private String id;
        private String username;
        private String phoneNumber;
        private String imgUrl;
        private String email;
        private String provider;
        private Integer money;
    }


}
