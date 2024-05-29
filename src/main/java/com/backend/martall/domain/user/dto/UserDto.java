package com.backend.martall.domain.user.dto;

import com.backend.martall.domain.user.entity.User;
import com.backend.martall.domain.user.entity.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

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

        public void ofKakao (UserKakaoRequestDto userKakaoRequestDto) {
            this.username = userKakaoRequestDto.getUsername();
            this.imgUrl = userKakaoRequestDto.getImgUrl();
            this.provider = userKakaoRequestDto.getProvider();
            this.providerId = userKakaoRequestDto.getProviderId();
            this.userType = userKakaoRequestDto.getUserType();
        }
    }

    @NoArgsConstructor
    @Data
    public static class UserKakaoRequestDto {
        private String username;
        private String imgUrl;
        private String provider;
        private Long providerId;
        private Integer userType;
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

    @Getter
    public static class UserJoinDto {

        @NotBlank(message = "이름은 필수 입력 값입니다.")
        private String name;

        @NotBlank(message = "아이디는 필수 입력 값입니다.")
        private String id;

        @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[!@#$%^&*()_+={}|:<>?]).{8,}$",
                message = "비밀번호는 문자, 숫자, 특수기호를 포함한 8자 이상이어야 합니다.")
        private String password;

        @NotBlank(message = "이메일은 필수 입력 값입니다.")
        @Email
        private String email;

    }

    @Getter
    public static class IdDupCheckRequestDto {

        @NotBlank(message = "아이디는 필수 입력 값입니다.")
        private String id;

    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IdDupCheckResponseDto {

        private boolean idDupCheck;

    }

}
