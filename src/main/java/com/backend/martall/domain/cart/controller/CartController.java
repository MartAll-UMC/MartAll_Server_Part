package com.backend.martall.domain.cart.controller;

import com.backend.martall.domain.cart.dto.CartItemAddRequestDto;
import com.backend.martall.domain.cart.dto.CartItemDeleteRequestDto;
import com.backend.martall.domain.cart.dto.CartItemUpdateRequestDto;
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
    public ResponseEntity<JsonResponse> addCart(@RequestBody CartItemAddRequestDto cartItemAddRequestDto) {
        Long userIdx = jwtTokenProvider.resolveToken();
        cartService.addCartItem(cartItemAddRequestDto, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    // 장바구니 수정
    @PatchMapping("/cart")
    public ResponseEntity<JsonResponse> updateCart(@RequestBody CartItemUpdateRequestDto cartItemUpdateRequestDto) {
        Long userIdx = jwtTokenProvider.resolveToken();
        cartService.updateCartItem(cartItemUpdateRequestDto, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    // 장바구니 상품 삭제
    @DeleteMapping("/cart")
    public ResponseEntity<JsonResponse> deleteCart(@RequestBody CartItemDeleteRequestDto cartItemDeleteRequestDto) {
        Long userIdx = jwtTokenProvider.resolveToken();
        cartService.deleteCartItem(cartItemDeleteRequestDto, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }
}
