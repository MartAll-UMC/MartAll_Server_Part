package com.backend.martall.domain.item.controller;

import com.backend.martall.domain.item.dto.ItemResponseDto;
import com.backend.martall.domain.item.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
