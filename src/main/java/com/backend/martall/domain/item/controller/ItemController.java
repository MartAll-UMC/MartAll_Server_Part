package com.backend.martall.domain.item.controller;

import com.backend.martall.domain.item.dto.*;
import com.backend.martall.domain.item.service.ItemService;
import com.backend.martall.global.dto.JsonResponse;
import com.backend.martall.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/item")
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/search")
    public ResponseEntity<JsonResponse> searchItems(@RequestParam String itemName) {
        List<ItemListResponseDto> itemListResponseDtos = itemService.searchItems(itemName);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, itemListResponseDtos));
    }

    @GetMapping("/{shopId}/{itemId}")
    public ResponseEntity<JsonResponse> getItemById(@PathVariable Long shopId, @PathVariable int itemId) {
        ItemDetailResponseDto itemDetailResponseDto = itemService.getItemDetail(shopId, itemId);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, itemDetailResponseDto));
    }

    @GetMapping("/new-item")
    public ResponseEntity<JsonResponse> newItems() {
        List<ItemNewResponseDto> itemNewResponseDtos = itemService.newItems();
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, itemNewResponseDtos));
    }

    @PostMapping("/test-add")
    public ResponseEntity<JsonResponse> addItem(@RequestBody ItemAddRequestDto itemAddRequestDto) {
        itemService.addItem(itemAddRequestDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }
}
