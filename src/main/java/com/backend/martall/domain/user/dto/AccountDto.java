package com.backend.martall.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

public class AccountDto {

    @Getter
    public static class IdInquiryEmailRequestDto {

        @NotBlank(message = "이메일을 입력해주세요.")
        @Email(message = "이메일 형식을 지켜주세요")
        private String email;

    }

    @Getter
    public static class IdInquiryVerificationCodeRequestDto {

        @NotBlank(message = "코드를 입력해주세요.")
        @Length(min = 0, max = 6)
        private String verificationCode;

    }

}
