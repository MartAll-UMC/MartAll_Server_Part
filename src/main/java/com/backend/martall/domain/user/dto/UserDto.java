package com.backend.martall.domain.user.dto;

import com.backend.martall.domain.user.entity.User;
import com.backend.martall.domain.user.entity.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(example = "1")
        private Long userId;
        @Schema(example = "사용자 이름")
        private String username;
        @Schema(example = "010xxxxxxxx")
        private String phoneNumber;
        @Schema(example = "이미지 경로")
        private String imgUrl;
        @Schema(example = "xxxxx@google.com")
        private String email;
        @Schema(example = "kakao")
        private String provider;
        @Schema(example = "USER")
        private UserType userType;

        public UserInfoResponseDto(User user) {
            this.userId = user.getUserIdx();
            this.username = user.getUsername();
            this.phoneNumber = user.getPhoneNumber();
            this.imgUrl = user.getImgUrl();
            this.email = user.getEmail();
            this.provider = user.getEmail();
            this.userType = UserType.findByTypeCode(user.getUserType());
        }

    }

    @NoArgsConstructor
    @Data
    public static class UserLocationDto {
        @Schema(example = "123")
        private Double longitude;
        @Schema(example = "321")
        private Double latitude;
        @Schema(example = "서울특별시 종로구 청와대로 1")
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
        @Schema(example = "123")
        private Double longitude;
        @Schema(example = "321")
        private Double latitude;
        @Schema(example = "서울특별시 종로구 청와대로 1")
        private String address;
        @Schema(example = "456")
        private Integer locationRange;
        public UserLocationRangeDto(User user) {
            this.longitude = user.getLongitude();
            this.latitude = user.getLatitude();
            this.address = user.getAddress();
            this.locationRange = user.getLocationRange();
        }

    }
}
