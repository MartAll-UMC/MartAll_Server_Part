package com.backend.martall.domain.item.controller;

import com.backend.martall.domain.item.dto.*;
import com.backend.martall.domain.item.service.ItemService;
import com.backend.martall.domain.user.jwt.JwtTokenProvider;
import com.backend.martall.global.dto.JsonResponse;
import com.backend.martall.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("/item")
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final JwtTokenProvider jwtTokenProvider;

    // 검색
    @GetMapping("/search")
    public ResponseEntity<JsonResponse> searchItems(@RequestParam(required = false) String keyword) {
        Long userIdx = jwtTokenProvider.resolveToken();
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, itemService.searchItems(keyword, userIdx)));
    }

    // 상세정보
    @GetMapping("/{shopId}/{itemId}")
    public ResponseEntity<JsonResponse> getItemById(@PathVariable Long shopId, @PathVariable int itemId) {
        Long userIdx = jwtTokenProvider.resolveToken();
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, itemService.getItemDetail(shopId, itemId, userIdx)));
    }

    // 새로나온 상품
    @GetMapping("/new-item")
    public ResponseEntity<JsonResponse> newItems() {
        Long userIdx = jwtTokenProvider.resolveToken();
        List<ItemNewResponseDto> itemNewResponseDtos = itemService.newItems(userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, itemNewResponseDtos));
    }

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
    @PostMapping("/test-add")
    public ResponseEntity<JsonResponse> addItem(@RequestBody ItemAddRequestDto itemAddRequestDto) {
        itemService.addItem(itemAddRequestDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @PostMapping("/test/add/{itemId}")
    public ResponseEntity<JsonResponse> addItem(@RequestPart(name = "profileImage") MultipartFile profileImage, @RequestPart(name = "contentImage") MultipartFile contentImage, @PathVariable int itemId) {
        itemService.addItemWithImage(profileImage, contentImage, itemId);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }
}
