package com.backend.yourapplication.admin.controller;

import com.backend.yourapplication.admin.dto.InquiryReplyRequestDto;
import com.backend.yourapplication.admin.dto.InquiryResponseDto;
import com.backend.yourapplication.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/inquiries")
@RequiredArgsConstructor
public class InqAdminController {

    private final AdminService adminService;

    // 1:1 문의 내역 조회 (관리자용)
    @GetMapping
    public ResponseEntity<List<InquiryResponseDto>> getAllInquiries() {
        List<InquiryResponseDto> responseDtos = adminService.getAllInquiries();
        return ResponseEntity.ok(responseDtos);
    }

    // 1:1 문의 답변 작성
    @PostMapping("/{inquiryId}/reply")
    public ResponseEntity<Void> replyToInquiry(@PathVariable Long inquiryId, @RequestBody InquiryReplyRequestDto requestDto) {
        adminService.replyToInquiry(inquiryId, requestDto);
        return ResponseEntity.ok().build();
    }

    // 1:1 문의 답변 수정 (관리자용)
    @PatchMapping("/{inquiryId}/reply")
    public ResponseEntity<Void> updateInquiryReply(@PathVariable Long inquiryId, @RequestBody InquiryReplyRequestDto requestDto) {
        adminService.updateInquiryReply(inquiryId, requestDto);
        return ResponseEntity.ok().build();
    }
}
