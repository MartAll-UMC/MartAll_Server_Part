package com.backend.martall.domain.inquiry.service;

import com.backend.martall.domain.inquiry.dto.InquiryRequestDto;
import com.backend.martall.domain.inquiry.dto.InquiryResponseDto;
import com.backend.martall.domain.inquiry.entity.Inquiry;
import com.backend.martall.domain.inquiry.entity.InquiryContent;
import com.backend.martall.domain.inquiry.repository.InquiryContentRepository;
import com.backend.martall.domain.inquiry.repository.InquiryRepository;
import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.mart.repository.MartRepository;
import com.backend.martall.domain.user.entity.User;
import com.backend.martall.domain.user.entity.UserRepository;
import com.backend.martall.global.exception.BadRequestException;
import com.backend.martall.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InquiryCommandService {

    private final UserRepository userRepository;
    private final InquiryRepository inquiryRepository;
    private final MartRepository martRepository;
    private final InquiryContentRepository inquiryContentRepository;


    // 문의 생성
    public InquiryResponseDto.InquiryIdResponseDto createInquiry(InquiryRequestDto.InquiryCreateRequestDto inquiryCreateRequestDto,
                                                                 Long userIdx) {

        // 문의한 회원
        User user = userRepository.findByUserIdx(userIdx).get();

        // 문의 마트
        MartShop martShop = martRepository.findById(inquiryCreateRequestDto.getMartId())
                .orElseThrow(() -> new BadRequestException(ResponseStatus.MART_NAME_NOT_FOUND));

        // 문의 생성
        Inquiry inquiry = inquiryRepository.save(Inquiry.builder()
                .user(user)
                .martShop(martShop)
                .build());

        // 문의 메세지 생성
        inquiryContentRepository.save(InquiryContent.builder()
                .content(inquiryCreateRequestDto.getContent())
                .isUser(true)
                .inquiry(inquiry)
                .build());

        return InquiryResponseDto.InquiryIdResponseDto.builder()
                .inquiryId(inquiry.getId())
                .build();
    }
}
