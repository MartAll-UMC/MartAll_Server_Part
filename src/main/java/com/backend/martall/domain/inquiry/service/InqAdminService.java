package com.backend.martall.domain.inquiry.service;

import com.backend.martall.domain.inquiry.dto.InquiryReplyRequestDto;
import com.backend.martall.domain.inquiry.entity.InquiryResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InqAdminService {

    private final InquiryResponseRepository inquiryResponseRepository;

    public InqAdminService(InquiryResponseRepository inquiryResponseRepository) {
        this.inquiryResponseRepository = inquiryResponseRepository;
    }

    @Transactional
    public void replyToInquiry(Long inquiryId, InquiryReplyRequestDto requestDto) {
        InquiryResponse inquiryResponse = new InquiryResponse();
        // Set the inquiry relation
        inquiryResponseRepository.save(inquiryResponse);
    }

    @Transactional
    public void updateInquiryReply(Long inquiryId, InquiryReplyRequestDto requestDto) {
        InquiryResponse inquiryResponse = inquiryResponseRepository.findByInquiryId(inquiryId)
                .orElseThrow(() -> new RuntimeException("Response not found"));
        inquiryResponseRepository.save(inquiryResponse);
    }
}
