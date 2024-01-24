package com.backend.martall.domain.cart.controller;

import com.backend.martall.domain.cart.dto.CartRequestDto;
import com.backend.martall.domain.cart.entity.CartItem;
import com.backend.martall.domain.cart.service.CartService;
import com.backend.martall.global.dto.JsonResponse;
import com.backend.martall.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // 장바구니 조회
    @GetMapping("/cart")
    public ResponseEntity<JsonResponse> inquiryCart() {
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, cartService.getAllCartItem()));
    }

    // 장바구니 상품 추가
    @PostMapping("/cart")
    public ResponseEntity<JsonResponse> addCart(@RequestBody CartRequestDto cartRequestDto) {
        cartService.addCartItem(cartRequestDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    // 장바구니 수정
    @PatchMapping("/cart/{cartItemId}")
    public ResponseEntity<JsonResponse> updateCart(@PathVariable long cartItemId, @RequestBody CartRequestDto cartRequestDto) {
        cartService.updateCartItem(cartItemId, cartRequestDto);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    // 장바구니 상품 삭제
    @DeleteMapping("/cart/{cartItemId}")
    public ResponseEntity<JsonResponse> deleteCart(@PathVariable long cartItemId) {
        cartService.deleteCartItem(cartItemId);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }
}
