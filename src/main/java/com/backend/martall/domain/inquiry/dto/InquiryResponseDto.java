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
}
