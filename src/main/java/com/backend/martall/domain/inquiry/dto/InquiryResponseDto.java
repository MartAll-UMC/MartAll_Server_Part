package com.backend.martall.domain.inquiry.dto;

import com.backend.martall.domain.inquiry.entity.Inquiry;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InquiryResponseDto {
    private Long inquiryId;
    private String name;
    private String content;
    private LocalDateTime regTime;
    private String inquiryState;

    public static InquiryResponseDto from(Inquiry inquiry) {
        InquiryResponseDto dto = new InquiryResponseDto();
        dto.setInquiryId(inquiry.getInquiryId());
        dto.setName(inquiry.getName());
        dto.setContent(inquiry.getContent());
        dto.setRegTime(inquiry.getRegTime());
        dto.setInquiryState(inquiry.getInquiryState());
        return dto;
    }
}
