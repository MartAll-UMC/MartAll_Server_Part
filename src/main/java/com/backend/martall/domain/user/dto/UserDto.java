package com.backend.martall.domain.user.dto;

import com.backend.martall.domain.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
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

        public UserInfoResponseDto(User user) {
            this.id = user.getId();
            this.username = user.getUsername();
            this.phoneNumber = user.getPhoneNumber();
            this.imgUrl = user.getImgUrl();
            this.email = user.getEmail();
            this.provider = user.getEmail();
            this.money = user.getMoney();
        }

    }

}
