package com.backend.martall.domain.item.controller;

import com.backend.martall.domain.item.dto.ItemResponseDto;
import com.backend.martall.domain.item.service.ItemService;
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
    public ResponseEntity<List<ItemResponseDto>> searchItems(@RequestParam String itemName) {
        List<ItemResponseDto> itemResponseDtos = itemService.searchItems(itemName);
        return ResponseEntity.ok(itemResponseDtos);
    }

    @GetMapping("/{shopId}/{itemId}")
    public ResponseEntity<ItemResponseDto> getItemById(@PathVariable Long shopId, @PathVariable int itemId) {
        ItemResponseDto itemResponseDto = itemService.getItemDetail(shopId, itemId);
        return ResponseEntity.ok(itemResponseDto);
    }

    @GetMapping("/new-item")
    public ResponseEntity<List<ItemResponseDto>> newItems() {
        List<ItemResponseDto> itemResponseDtos = itemService.newItems();
        return ResponseEntity.ok(itemResponseDtos);
    }
}
