package com.backend.martall.domain.itemlike.controller;

import com.backend.martall.domain.itemlike.service.ItemLikeService;
import com.backend.martall.global.dto.JsonResponse;
import com.backend.martall.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/item-like")
@RequiredArgsConstructor
public class ItemLikeController {

    private final ItemLikeService itemLikeService;

    @GetMapping("")
    public ResponseEntity<JsonResponse> inquiryItemLike() {
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @PostMapping("/{itemId}")
    public ResponseEntity<JsonResponse> addItemLike(@PathVariable int itemId) {
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<JsonResponse> removeItemLike(@PathVariable int itemId) {
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }
}
