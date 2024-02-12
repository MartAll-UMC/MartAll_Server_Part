package com.backend.martall.domain.user.dto;

import com.backend.martall.domain.user.entity.User;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @NoArgsConstructor
    @Data
    public static class UserLocationDto {
        private Double longitude;
        private Double latitude;
        private String address;

        public UserLocationDto(User user) {
            this.longitude = user.getLongitude();
            this.latitude = user.getLatitude();
            this.address = user.getAddress();
        }
    }

    @NoArgsConstructor
    @Data
    public static class UserLocationRangeDto {
        private Double longitude;
        private Double latitude;
        private String address;
        private Integer locationRange;
        public UserLocationRangeDto(User user) {
            this.longitude = user.getLongitude();
            this.latitude = user.getLatitude();
            this.address = user.getAddress();
            this.locationRange = user.getLocationRange();
        }

    }
}
