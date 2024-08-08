package com.backend.martall.domain.inquiry.controller;


import com.backend.martall.domain.inquiry.dto.InquiryRequestDto;
import com.backend.martall.domain.inquiry.dto.InquiryResponseDto;
import com.backend.martall.domain.inquiry.service.InquiryCommandService;
import com.backend.martall.domain.user.jwt.JwtTokenProvider;
import com.backend.martall.global.dto.JsonResponse;
import com.backend.martall.global.exception.ResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inquiry")
@RequiredArgsConstructor
public class InquiryController {

    private final JwtTokenProvider jwtTokenProvider;
    private final InquiryCommandService inquiryCommandService;

    @Operation(summary = "문의 생성")
    @ApiResponse(responseCode = "200", description = "문의 생성",useReturnTypeSchema = true)
    @PostMapping("/")
    public ResponseEntity<JsonResponse<InquiryResponseDto.InquiryIdResponseDto>> createInquiry(@RequestBody InquiryRequestDto.InquiryCreateRequestDto inquiryCreateRequestDto) {

        Long userIdx = jwtTokenProvider.resolveToken();

        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, inquiryCommandService.createInquiry(inquiryCreateRequestDto, userIdx)));

    }
}
