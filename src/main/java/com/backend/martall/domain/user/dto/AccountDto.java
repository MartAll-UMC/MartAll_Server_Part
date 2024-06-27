package com.backend.martall.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.validator.constraints.Length;

public class AccountDto {

    @Getter
    public static class IdInquiryEmailRequestDto {

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식을 지켜주세요")
        private String email;

    }

    @Getter
    public static class IdInquiryCertificationCodeRequestDto {

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식을 지켜주세요")
        private String email;

        @NotBlank(message = "코드를 입력해주세요.")
        @Length(min = 0, max = 6)
        private String verificationCode;

    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IdInquiryCertificationCodeResponseDto {

        private boolean emailCheck;
    }

    @Getter
    public static class IdInquiryRequestDto {

        @NotBlank(message = "이름을 입력해주세요.")
        private String name;

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식을 지켜주세요")
        private String email;

    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IdInquiryResponseDto {

        private String id;
        private String registerDate;

    }

    @Getter
    public static class PwdInquiryRequestDto {

        @NotBlank(message = "이름을 입력해주세요.")
        private String name;

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식을 지켜주세요")
        private String email;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PwdInquiryResponseDto {
        private boolean pwdInfoCheck;
    }

}
