package com.backend.martall.domain.cart.controller;

import com.backend.martall.domain.cart.dto.CartItemRequest;
import com.backend.martall.domain.cart.dto.CartItemRequestList;
import com.backend.martall.domain.cart.service.CartService;
import com.backend.martall.domain.user.jwt.JwtTokenProvider;
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
    private final JwtTokenProvider jwtTokenProvider;

    // 장바구니 조회
    @GetMapping("/cart")
    public ResponseEntity<JsonResponse> inquiryCart() {
        Long userIdx = jwtTokenProvider.resolveToken();
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, cartService.inquiryCart(userIdx)));
    }

    // 장바구니 상품 추가
    @PostMapping("/cart")
    public ResponseEntity<JsonResponse> addCart(@RequestBody CartItemRequest cartItemRequest) {
        Long userIdx = jwtTokenProvider.resolveToken();
        cartService.addCartItem(cartItemRequest, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    // 장바구니 수정
    @PatchMapping("/cart")
    public ResponseEntity<JsonResponse> updateCart(@RequestBody CartItemRequest cartItemRequest) {
        Long userIdx = jwtTokenProvider.resolveToken();
        cartService.updateCartItem(cartItemRequest, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    // 장바구니 상품 삭제
    @DeleteMapping("/cart")
    public ResponseEntity<JsonResponse> deleteCart(@RequestBody CartItemRequestList cartItemRequestList) {
        Long userIdx = jwtTokenProvider.resolveToken();
        cartService.deleteCartItem(cartItemRequestList, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }
}
