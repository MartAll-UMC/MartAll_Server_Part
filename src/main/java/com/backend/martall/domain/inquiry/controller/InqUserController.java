package com.backend.yourapplication.user.controller;

import com.backend.yourapplication.user.dto.InquiryRequestDto;
import com.backend.yourapplication.user.dto.InquiryResponseDto;
import com.backend.yourapplication.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/inquiries")
@RequiredArgsConstructor
public class InqUserController {

    private final UserService userService;

    // 1:1 문의 생성
    @PostMapping
    public ResponseEntity<InquiryResponseDto> createInquiry(@RequestBody InquiryRequestDto requestDto) {
        InquiryResponseDto responseDto = userService.createInquiry(requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 1:1 문의 수정
    @PatchMapping("/{inquiryId}")
    public ResponseEntity<InquiryResponseDto> updateInquiry(@PathVariable Long inquiryId, @RequestBody InquiryRequestDto requestDto) {
        InquiryResponseDto responseDto = userService.updateInquiry(inquiryId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 1:1 문의 내역 조회 (사용자용)
    @GetMapping
    public ResponseEntity<List<InquiryResponseDto>> getUserInquiries() {
        List<InquiryResponseDto> responseDtos = userService.getUserInquiries();
        return ResponseEntity.ok(responseDtos);
    }
}
