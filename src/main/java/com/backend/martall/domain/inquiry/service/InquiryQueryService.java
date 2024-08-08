package com.backend.martall.domain.inquiry.service;

import com.backend.martall.domain.inquiry.dto.InquiryResponseDto;
import com.backend.martall.domain.inquiry.entity.Inquiry;
import com.backend.martall.domain.inquiry.repository.InquiryRepository;
import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.mart.repository.MartRepository;
import com.backend.martall.domain.user.entity.User;
import com.backend.martall.domain.user.entity.UserRepository;
import com.backend.martall.global.exception.BadRequestException;
import com.backend.martall.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
@RequiredArgsConstructor
public class InquiryQueryService {

    private final UserRepository userRepository;
    private final MartRepository martRepository;
    private final InquiryRepository inquiryRepository;

    public List<InquiryResponseDto.InquiryListResponseDto> getInquiryList(Boolean isMart,
                                                                          Long userIdx) {

        User user = userRepository.findByUserIdx(userIdx).get();

        // 손님 기준, 사장님 기준으로 문의 찾기
        // 손님이면 title에 마트이름, 사장님으면 title에 손님 이름
        if (isMart) {
            MartShop martShop = martRepository.findByUser(user)
                    .orElseThrow(() -> new BadRequestException(ResponseStatus.MART_NAME_NOT_FOUND));

            List<Inquiry> inquiryList = inquiryRepository.findByMartShopWithInquiryContentList(martShop);

            return inquiryList.stream()
                    .map(inquiry -> InquiryResponseDto.InquiryListResponseDto.builder()
                            .title(inquiry.getUser().getUsername() + " 고객님")
                            .content(inquiry.getInquiryContentList().get(inquiry.getInquiryContentList().size() - 1).getContent())
                            .updatedAt(formatDateTime(inquiry.getInquiryContentList().get(inquiry.getInquiryContentList().size() - 1).getUpdatedAt()))
                            .build())
                    .toList();
        } else {
            List<Inquiry> inquiryList = inquiryRepository.findByUserWithInquiryContentList(user);

            return inquiryList.stream()
                    .map(inquiry -> InquiryResponseDto.InquiryListResponseDto.builder()
                            .title(inquiry.getMartShop().getName())
                            .content(inquiry.getInquiryContentList().get(inquiry.getInquiryContentList().size() - 1).getContent())
                            .updatedAt(formatDateTime(inquiry.getInquiryContentList().get(inquiry.getInquiryContentList().size() - 1).getUpdatedAt()))
                            .build())
                    .toList();
        }

    }

    public String formatDateTime(LocalDateTime dateTime) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 M월 d일 a h시 m분", Locale.KOREA);

        return dateTime.format(formatter);
    }

    public List<InquiryResponseDto.InquiryContentResponseDto> getInquiryContent(Long inquiryId,
                                                                                Long userIdx) {

        User user = userRepository.findByUserIdx(userIdx).get();

        Inquiry inquiry = inquiryRepository.findByInquiryIdWithContent(inquiryId)
                .orElseThrow(() -> new BadRequestException(ResponseStatus.INQUIRY_NOT_EXIST));


        if (inquiry.getUser().equals(user)) {

            return inquiry.getInquiryContentList().stream()
                    .map(inquiryContent -> InquiryResponseDto.InquiryContentResponseDto.builder()
                            .isCurrentUser(inquiryContent.getIsUser())
                            .content(inquiryContent.getContent())
                            .build())
                    .toList();

        } else if (inquiry.getMartShop().getUser().equals(user)) {

            return inquiry.getInquiryContentList().stream()
                    .map(inquiryContent -> InquiryResponseDto.InquiryContentResponseDto.builder()
                            .isCurrentUser(!inquiryContent.getIsUser())
                            .content(inquiryContent.getContent())
                            .build())
                    .toList();

        } else {
            throw new BadRequestException(ResponseStatus.INQUIRY_NOT_MATCH);
        }
    }
}
