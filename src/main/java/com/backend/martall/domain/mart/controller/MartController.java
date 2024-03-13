package com.backend.martall.domain.mart.controller;


import java.util.List;

import com.backend.martall.domain.mart.dto.*;
import com.backend.martall.domain.mart.service.MartService;
import com.backend.martall.global.dto.JsonResponse;
import com.backend.martall.domain.user.jwt.JwtTokenProvider;
import com.backend.martall.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/mart/shops")
@RequiredArgsConstructor
public class MartController {

    private final MartService martService;
    private final JwtTokenProvider jwtTokenProvider;

    // 마트 생성
    @PostMapping
    public ResponseEntity<JsonResponse> createMart(@RequestBody MartRequestDto requestDto) {
        martService.createMart(requestDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    // 마트 정보 업데이트
    @PatchMapping("/{shopId}")
    public ResponseEntity<JsonResponse> updateMartShop(@PathVariable Long shopId, @RequestBody MartRequestDto requestDto) {
        Long userIdx = jwtTokenProvider.resolveToken();
        MartUpdateResponseDto responseDto = martService.updateMart(shopId, requestDto, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, responseDto));
    }

    // 마트 검색
    @GetMapping("/search")
    public ResponseEntity<JsonResponse> searchMarts(@RequestParam String keyword) {
        Long userIdx = jwtTokenProvider.resolveToken();
        List<MartResponseDto> responseDtos = martService.searchMarts(keyword, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, responseDtos));
    }

    // 마트 상세 정보 조회
    @GetMapping("/{shopId}/detail")
    public ResponseEntity<JsonResponse> getMartDetail(@PathVariable Long shopId) {
        Long userIdx = jwtTokenProvider.resolveToken();
        MartDetailResponseDto martDetailResponseDto = martService.getMartDetail(shopId, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, martDetailResponseDto));
    }


    //마트 검색 by filter
    @GetMapping("/search/filter")
    public ResponseEntity<JsonResponse> searchMartsWithFilters(
            @RequestParam(defaultValue = "전체") String tag,
            @RequestParam(required = false) Integer minBookmark,
            @RequestParam(required = false) Integer maxBookmark,
            @RequestParam(required = false) Integer minLike,
            @RequestParam(required = false) Integer maxLike,
            @RequestParam(defaultValue = "기본") String sort) {
        Long userIdx = jwtTokenProvider.resolveToken();
        List<MartWithItemResponseDto> responseDtos = martService.searchMartsByCategoryAndRating(tag, minBookmark, maxBookmark, minLike, maxLike, sort, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, responseDtos));
    }

    @GetMapping("/all")
    public ResponseEntity<JsonResponse> getAllMarts() {
        Long userIdx = jwtTokenProvider.resolveToken();
        List<MartWithItemResponseDto> marts = martService.findAllMarts(userIdx);
        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, marts));
    }
}