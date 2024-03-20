package com.backend.martall.domain.item.controller;

import com.backend.martall.domain.item.dto.*;
import com.backend.martall.domain.item.service.ItemService;
import com.backend.martall.domain.mart.dto.MartResponseDto;
import com.backend.martall.domain.user.jwt.JwtTokenProvider;
import com.backend.martall.global.dto.JsonResponse;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Item", description = "Item API")
@RequestMapping("/item")
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final JwtTokenProvider jwtTokenProvider;

    // 검색
    @Operation(summary = "상품 키워드 검색")
    @Parameter(name = "keyword", description = "검색할 키워드")
    @ApiResponse(responseCode = "200", description = "상품 키워드 검색 목록",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ItemKeywordSearchResponseDto.class))))
    @GetMapping("/search")
    public ResponseEntity<JsonResponse> searchItems(@RequestParam(required = false) String keyword) {
        Long userIdx = jwtTokenProvider.resolveToken();
        List<ItemKeywordSearchResponseDto> itemKeywordSearchResponseDtoList = itemService.searchItems(keyword, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, itemKeywordSearchResponseDtoList));
    }

    // 상세정보
    @Operation(summary = "상품 상세정보")
    @Parameter(name = "shopId", description = "마트의 아이디")
    @Parameter(name = "itemId", description = "상품의 아이디")
    @ApiResponse(responseCode = "200", description = "상품 상세정보",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = ItemDetailResponseDto.class)))
    @GetMapping("/{shopId}/{itemId}")
    public ResponseEntity<JsonResponse> getItemById(@PathVariable Long shopId, @PathVariable int itemId) {
        Long userIdx = jwtTokenProvider.resolveToken();
        ItemDetailResponseDto itemDetailResponseDto = itemService.getItemDetail(shopId, itemId, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, itemDetailResponseDto));
    }

    // 새로나온 상품
    @Operation(summary = "새로 나온 상품 조회")
    @ApiResponse(responseCode = "200", description = "새로 나온 상품 목록",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ItemNewResponseDto.class))))
    @GetMapping("/new-item")
    public ResponseEntity<JsonResponse> newItems() {
        Long userIdx = jwtTokenProvider.resolveToken();
        List<ItemNewResponseDto> itemNewResponseDtos = itemService.newItems(userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, itemNewResponseDtos));
    }

    // 아이템 카테고리 검색
    @Operation(summary = "상품 카테고리 검색")
    @Parameter(name = "category", description = "상품 카테고리")
    @Parameter(name = "minPrice", description = "최소 가격")
    @Parameter(name = "maxPrice", description = "최대 가격")
    @Parameter(name = "sort", description = "정렬 기준")
    @ApiResponse(responseCode = "200", description = "상품 카테고리 검색 목록",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = ItemCategorySearchResponseDto.class))))
    @GetMapping("/category")
    public ResponseEntity<JsonResponse> categoryItems(@RequestParam(defaultValue = "전체") String category,
                                                      @RequestParam(required = false) Integer minPrice,
                                                      @RequestParam(required = false) Integer maxPrice,
                                                      @RequestParam(defaultValue = "기본") String sort) {
        Long userIdx = jwtTokenProvider.resolveToken();
        List<ItemCategorySearchResponseDto> itemCategoryResponseDtoList = itemService.getCategoryItem(category, minPrice, maxPrice, sort, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, itemCategoryResponseDtoList));
    }

    // 상품 추가 테스트 컨트롤러
    @Hidden
    @PostMapping("/test-add")
    public ResponseEntity<JsonResponse> addItem(@RequestBody ItemAddRequestDto itemAddRequestDto) {
        itemService.addItem(itemAddRequestDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @Hidden
    @PostMapping("/test/add/{itemId}")
    public ResponseEntity<JsonResponse> addItem(@RequestPart(name = "profileImage") MultipartFile profileImage, @RequestPart(name = "contentImage") MultipartFile contentImage, @PathVariable int itemId) {
        itemService.addItemWithImage(profileImage, contentImage, itemId);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }
}
