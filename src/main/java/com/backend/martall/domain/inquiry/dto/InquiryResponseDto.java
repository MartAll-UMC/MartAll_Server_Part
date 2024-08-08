package com.backend.martall.domain.inquiry.dto;

import lombok.*;

public class InquiryResponseDto {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InquiryIdResponseDto {
        private Long inquiryId;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InquiryListResponseDto {
        private Long inquiryId;
        private String title;
        private String content;
        private String updatedAt;
    }
}
