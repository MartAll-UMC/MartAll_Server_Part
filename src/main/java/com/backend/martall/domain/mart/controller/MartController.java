package com.backend.martall.domain.mart.controller;


import com.backend.martall.domain.mart.dto.*;
import com.backend.martall.domain.mart.service.MartService;
import com.backend.martall.domain.user.jwt.JwtTokenProvider;
import com.backend.martall.global.dto.JsonResponse;
import com.backend.martall.global.exception.ResponseStatus;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Tag(name = "Mart", description = "Mart API")
@RestController
@RequestMapping("/mart/shops")
@RequiredArgsConstructor
public class MartController {

    private final MartService martService;
    private final JwtTokenProvider jwtTokenProvider;


    // 마트 생성
    @Hidden
    @PostMapping
    public ResponseEntity<JsonResponse> createMart(@RequestBody MartRequestDto requestDto) {
        martService.createMartTest(requestDto);
        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, null));
    }

    // 마트 정보 업데이트
    @Hidden
    @PatchMapping("/{shopId}")
    public ResponseEntity<JsonResponse<MartUpdateResponseDto>> updateMartShop(@PathVariable Long shopId, @RequestBody MartRequestDto requestDto) {
        Long userIdx = jwtTokenProvider.resolveToken();

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, martService.updateMart(shopId, requestDto, userIdx)));
    }

    // 마트 검색
    @Operation(summary = "마트 키워드 검색")
    @Parameter(name = "keyword", description = "검색할 키워드")
    @ApiResponse(responseCode = "200", description = "마트 키워드 검색 목록", useReturnTypeSchema = true)
    @GetMapping("/search")
    public ResponseEntity<JsonResponse<List<MartResponseDto>>> searchMarts(@RequestParam String keyword) {
        Long userIdx = jwtTokenProvider.resolveToken();

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, martService.searchMarts(keyword, userIdx)));
    }

    @Operation(summary = "마트 상세정보")
    @Parameter(name = "shopId", description = "마트의 아이디")
    @ApiResponse(responseCode = "200", description = "마트 상세정보", useReturnTypeSchema = true)
    // 마트 상세 정보 조회
    @GetMapping("/{shopId}/detail")
    public ResponseEntity<JsonResponse<MartDetailResponseDto>> getMartDetail(@PathVariable Long shopId) {
        Long userIdx = jwtTokenProvider.resolveToken();

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, martService.getMartDetail(shopId, userIdx)));
    }


    //마트 검색 by filter
    @Operation(summary = "마트 필터 검색")
    @Parameter(name = "tag", description = "검색할 마트의 태그 - 전체, 육아용품, 화장품, 식품, 수산, 건강식품, 반려동물, 정육")
    @Parameter(name = "minBookmark", description = "최소 단골마트 등록 수")
    @Parameter(name = "maxBookmark", description = "최대 단골마트 등록 수")
    @Parameter(name = "minLike", description = "최소 마트 상품 찜 수")
    @Parameter(name = "maxLike", description = "최대 마트 상품 찜 수")
    @Parameter(name = "sort", description = "정렬 기준 - 기본, 최신, 단골, 찜")
    @ApiResponse(responseCode = "200", description = "마트 필터 검색 목록", useReturnTypeSchema = true)
    @GetMapping("/search/filter")
    public ResponseEntity<JsonResponse<List<MartWithItemResponseDto>>> searchMartsWithFilters(
            @RequestParam(defaultValue = "전체") String tag,
            @RequestParam(required = false) Integer minBookmark,
            @RequestParam(required = false) Integer maxBookmark,
            @RequestParam(required = false) Integer minLike,
            @RequestParam(required = false) Integer maxLike,
            @RequestParam(defaultValue = "기본") String sort) {
        Long userIdx = jwtTokenProvider.resolveToken();

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, martService.searchMartsByCategoryAndRating(tag, minBookmark, maxBookmark, minLike, maxLike, sort, userIdx)));
    }

    @Operation(summary = "마트 전체 조회")
    @ApiResponse(responseCode = "200", description = "마트 전체 조회 목록", useReturnTypeSchema = true)
    @GetMapping("/all")
    public ResponseEntity<JsonResponse<List<MartWithItemResponseDto>>> getAllMarts() {
        Long userIdx = jwtTokenProvider.resolveToken();

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, martService.findAllMarts(userIdx)));
    }

    @Operation(summary = "마트 상품 조회")
    @ApiResponse(responseCode = "200", description = "마트 상품 조회", useReturnTypeSchema = true)
    @GetMapping("/{shopId}/item")
    public ResponseEntity<JsonResponse<List<MartItemResponseDto>>> getMartItem(@PathVariable Long shopId) {
        Long userIdx = jwtTokenProvider.resolveToken();
        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, martService.getMartItem(shopId, userIdx)));
    }

//    @Operation(summary = "마트 랜덤 추천")
//    @ApiResponse(responseCode = "200", description = "마트 랜덤 추천 목록", useReturnTypeSchema = true)
//    @GetMapping("/recommended")
//    public ResponseEntity<JsonResponse<List<MartRecommendedResponseDto>>> getRecommendedMart() {
//        List<MartRecommendedResponseDto> martRecommendedResponseDtoList = martService.getRecommendedMart();
//        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, martRecommendedResponseDtoList));
//    }

    @Operation(summary = "오늘의 마트")
    @ApiResponse(responseCode = "200", description = "마트 랜덤 추천 목록", useReturnTypeSchema = true)
    @GetMapping("/today")
    public ResponseEntity<JsonResponse<List<MartRecommendedResponseDto>>> todayMart() {

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, martService.getTodayMart()));
    }

    @Operation(summary = "마트 검색 키워드 추천")
    @ApiResponse(responseCode = "200", description = "마트 검색 키워드 추천 목록", useReturnTypeSchema = true)
    @GetMapping("/recommendKeyword")
    public ResponseEntity<JsonResponse<List<String>>> martKeyword() {

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, martService.recommendMartKeyword()));
    }

    @Operation(summary = "가게 등록 여부 조회")
    @ApiResponse(responseCode = "200", description = "가게 등록 여부 조회", useReturnTypeSchema = true)
    @GetMapping("/exist")
    public ResponseEntity<JsonResponse<MartExistResponseDto>> checkOwnMart() {

        Long userIdx = jwtTokenProvider.resolveToken();

        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, martService.checkOwnMart(userIdx)));
    }
}