package com.backend.martall.domain.mart.controller;


import java.util.List;

import com.backend.martall.domain.item.dto.ItemMartNewResponseDto;
import com.backend.martall.domain.mart.dto.*;
import com.backend.martall.domain.mart.service.MartService;
import com.backend.martall.global.dto.JsonResponse;
import com.backend.martall.domain.user.jwt.JwtTokenProvider;
import com.backend.martall.global.exception.ResponseStatus;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name="Mart", description = "Mart API")
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
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    // 마트 정보 업데이트
    @Hidden
    @PatchMapping("/{shopId}")
    public ResponseEntity<JsonResponse> updateMartShop(@PathVariable Long shopId, @RequestBody MartRequestDto requestDto) {
        Long userIdx = jwtTokenProvider.resolveToken();
        MartUpdateResponseDto responseDto = martService.updateMart(shopId, requestDto, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, responseDto));
    }

    // 마트 검색
    @Operation(summary = "마트 키워드 검색")
    @Parameter(name = "keyword", description = "검색할 키워드")
    @ApiResponse(responseCode = "200", description = "마트 키워드 검색 목록",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = MartResponseDto.class))))
    @GetMapping("/search")
    public ResponseEntity<JsonResponse> searchMarts(@RequestParam String keyword) {
        Long userIdx = jwtTokenProvider.resolveToken();
        List<MartResponseDto> responseDtos = martService.searchMarts(keyword, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, responseDtos));
    }

    @Operation(summary = "마트 상세정보")
    @Parameter(name = "shopId", description = "마트의 아이디")
    @ApiResponse(responseCode = "200", description = "마트 상세정보",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = MartDetailResponseDto.class)))
    // 마트 상세 정보 조회
    @GetMapping("/{shopId}/detail")
    public ResponseEntity<JsonResponse> getMartDetail(@PathVariable Long shopId) {
        Long userIdx = jwtTokenProvider.resolveToken();
        MartDetailResponseDto martDetailResponseDto = martService.getMartDetail(shopId, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, martDetailResponseDto));
    }


    //마트 검색 by filter
    @Operation(summary = "마트 필터 검색")
    @Parameter(name = "tag", description = "검색할 마트의 태그 - 전체, 육아용품, 화장품, 식품, 수산, 건강식품, 반려동물, 정육")
    @Parameter(name = "minBookmark", description = "최소 단골마트 등록 수")
    @Parameter(name = "maxBookmark", description = "최대 단골마트 등록 수")
    @Parameter(name = "minLike", description = "최소 마트 상품 찜 수")
    @Parameter(name = "maxLike", description = "최대 마트 상품 찜 수")
    @Parameter(name = "sort", description = "정렬 기준 - 기본, 최신, 단골, 찜")
    @ApiResponse(responseCode = "200", description = "마트 필터 검색 목록",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = MartWithItemResponseDto.class) )))
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

    @Operation(summary = "마트 전체 조회")
    @ApiResponse(responseCode = "200", description = "마트 전체 조회 목록",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = MartWithItemResponseDto.class) )))
    @GetMapping("/all")
    public ResponseEntity<JsonResponse> getAllMarts() {
        Long userIdx = jwtTokenProvider.resolveToken();
        List<MartWithItemResponseDto> marts = martService.findAllMarts(userIdx);
        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, marts));
    }

    @Operation(summary = "마트 상품 조회")
    @ApiResponse(responseCode = "200", description = "마트 상품 조회",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ItemMartNewResponseDto.class) )))
    @GetMapping("/{shopId}/item")
    public ResponseEntity<JsonResponse> getMartItem(@PathVariable Long shopId){
        Long userIdx = jwtTokenProvider.resolveToken();
        List<ItemMartNewResponseDto> itemMartNewResponseDtoList = martService.getMartItem(shopId, userIdx);
        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, itemMartNewResponseDtoList));
    }
}