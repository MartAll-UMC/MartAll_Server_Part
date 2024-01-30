package com.backend.martall.domain.inquiry.service;

import com.backend.martall.domain.inquiry.dto.InquiryRequestDto;
import com.backend.martall.domain.inquiry.dto.InquiryResponseDto;
import com.backend.martall.domain.inquiry.entity.Inquiry;
import com.backend.martall.domain.inquiry.repository.InquiryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InqUserService {

    private final InquiryRepository inquiryRepository;

    public InqUserService(InquiryRepository inquiryRepository) {
        this.inquiryRepository = inquiryRepository;
    }

    @Transactional
    public InquiryResponseDto createInquiry(InquiryRequestDto requestDto) {
        Inquiry inquiry = new Inquiry();
        Inquiry savedInquiry = inquiryRepository.save(inquiry);
        return new InquiryResponseDto();
    }

    @Transactional
    public InquiryResponseDto updateInquiry(Long inquiryId, InquiryRequestDto requestDto) {
        Inquiry inquiry = inquiryRepository.findById(inquiryId)
                .orElseThrow(() -> new RuntimeException("Inquiry not found"));
        return new InquiryResponseDto();
    }

    public List<InquiryResponseDto> getUserInquiries() {
        List<Inquiry> inquiries = inquiryRepository.findAll();
        return inquiries.stream()
                .map(inquiry -> new InquiryResponseDto())
                .collect(Collectors.toList());
    }
}
