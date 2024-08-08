package com.backend.martall.domain.inquiry.dto;

import lombok.Getter;

public class InquiryRequestDto {
    @Getter
    public static class InquiryCreateRequestDto {
        private Long martId;
        private String content;
    }
}
