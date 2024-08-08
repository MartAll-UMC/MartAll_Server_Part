package com.backend.martall.domain.inquiry.controller;


import com.backend.martall.domain.inquiry.dto.InquiryRequestDto;
import com.backend.martall.domain.inquiry.dto.InquiryResponseDto;
import com.backend.martall.domain.inquiry.service.InquiryCommandService;
import com.backend.martall.domain.inquiry.service.InquiryQueryService;
import com.backend.martall.domain.user.jwt.JwtTokenProvider;
import com.backend.martall.global.dto.JsonResponse;
import com.backend.martall.global.exception.ResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inquiry")
@RequiredArgsConstructor
public class InquiryController {

    private final JwtTokenProvider jwtTokenProvider;
    private final InquiryCommandService inquiryCommandService;
    private final InquiryQueryService inquiryQueryService;

    @Operation(summary = "문의 생성")
    @ApiResponse(responseCode = "200", description = "문의 생성",useReturnTypeSchema = true)
    @PostMapping("")
    public ResponseEntity<JsonResponse<InquiryResponseDto.InquiryIdResponseDto>> createInquiry(@RequestBody InquiryRequestDto.InquiryCreateRequestDto inquiryCreateRequestDto) {

        Long userIdx = jwtTokenProvider.resolveToken();

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, inquiryCommandService.createInquiry(inquiryCreateRequestDto, userIdx)));

    }

    @Operation(summary = "문의 작성")
    @ApiResponse(responseCode = "200", description = "문의 작성",useReturnTypeSchema = true)
    @PostMapping("/{inquiryId}")
    public ResponseEntity<JsonResponse<InquiryResponseDto.InquiryIdResponseDto>> createInquiryContent(@RequestBody InquiryRequestDto.InquiryContentRequestDto inquiryContentRequestDto,
                                                                                                      @PathVariable Long inquiryId) {

        Long userIdx = jwtTokenProvider.resolveToken();

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, inquiryCommandService.createInquiryContent(inquiryContentRequestDto, inquiryId, userIdx)));

    }

    @Operation(summary = "문의 목록")
    @ApiResponse(responseCode = "200", description = "문의 목록",useReturnTypeSchema = true)
    @GetMapping("")
    public ResponseEntity<JsonResponse<List<InquiryResponseDto.InquiryListResponseDto>>> getInquiryList(@RequestParam(defaultValue = "false") Boolean isMart) {

        Long userIdx = jwtTokenProvider.resolveToken();

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, inquiryQueryService.getInquiryList(isMart, userIdx)));

    }
}
