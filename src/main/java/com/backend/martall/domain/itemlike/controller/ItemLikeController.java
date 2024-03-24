package com.backend.martall.domain.itemlike.controller;

import com.backend.martall.domain.item.dto.ItemKeywordSearchResponseDto;
import com.backend.martall.domain.itemlike.dto.ItemLikeInquiryResponse;
import com.backend.martall.domain.itemlike.service.ItemLikeService;
import com.backend.martall.domain.user.jwt.JwtTokenProvider;
import com.backend.martall.global.dto.JsonResponse;
import com.backend.martall.global.exception.ResponseStatus;
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

import java.util.List;

@Tag(name = "ItemLike", description = "ItemLike API")
@RestController
@RequestMapping("/item-like")
@RequiredArgsConstructor
public class ItemLikeController {

    private final ItemLikeService itemLikeService;
    private final JwtTokenProvider jwtTokenProvider;

    // 찜 상품 조회
    @Operation(summary = "찜한 상품 목록 조회")
    @ApiResponse(responseCode = "200", description = "찜 상품 조회 목록", useReturnTypeSchema = true)
    @GetMapping("")
    public ResponseEntity<JsonResponse<List<ItemLikeInquiryResponse>>> inquiryItemLike() {
        Long userIdx = jwtTokenProvider.resolveToken();
        List<ItemLikeInquiryResponse> itemLikeInquiryResponseList = itemLikeService.inquiryItemLike(userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, itemLikeInquiryResponseList));
    }


    // 찜 상품 추가
    @Operation(summary = "상품 찜하기")
    @Parameter(name = "itemId", description = "아이템 아이디")
    @PostMapping("/{itemId}")
    public ResponseEntity<JsonResponse> addItemLike(@PathVariable int itemId) {
        Long userIdx = jwtTokenProvider.resolveToken();
        itemLikeService.addItemLike(itemId, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @Operation(summary = "상품 찜 취소")
    @Parameter(name = "itemId", description = "아이템 아이디")
    @DeleteMapping("/{itemId}")
    public ResponseEntity<JsonResponse> removeItemLike(@PathVariable int itemId) {
        Long userIdx = jwtTokenProvider.resolveToken();
        itemLikeService.removeItemLike(itemId, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }
}
