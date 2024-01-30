package com.backend.martall.domain.inquiry.dto;

import com.backend.martall.domain.inquiry.entity.Inquiry;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class InquiryRequestDto {
    private String name;
    private String content;

    public Inquiry toEntity() {
        return new Inquiry(name, content);
    }
}
