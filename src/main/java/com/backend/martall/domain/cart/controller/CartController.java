package com.backend.martall.domain.cart.controller;

import com.backend.martall.domain.cart.dto.CartInquiryResponseDto;
import com.backend.martall.domain.cart.dto.CartItemAddRequestDto;
import com.backend.martall.domain.cart.dto.CartItemDeleteRequestDto;
import com.backend.martall.domain.cart.dto.CartItemUpdateRequestDto;
import com.backend.martall.domain.cart.service.CartService;
import com.backend.martall.domain.item.dto.ItemKeywordSearchResponseDto;
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

@Tag(name = "Cart", description = "Cart API")
@RequestMapping("/user")
@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final JwtTokenProvider jwtTokenProvider;

    // 장바구니 조회
    @Operation(summary = "장바구니 조회")
    @ApiResponse(responseCode = "200", description = "장바구니 조회", useReturnTypeSchema = true)
    @GetMapping("/cart")
    public ResponseEntity<JsonResponse<CartInquiryResponseDto>> inquiryCart() {
        Long userIdx = jwtTokenProvider.resolveToken();
        CartInquiryResponseDto cartInquiryResponseDto = cartService.inquiryCart(userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, cartInquiryResponseDto));
    }

    // 장바구니 상품 추가
    @Operation(summary = "장바구니 상품 추가")
    @PostMapping("/cart")
    public ResponseEntity<JsonResponse> addCart(@RequestBody CartItemAddRequestDto cartItemAddRequestDto) {
        Long userIdx = jwtTokenProvider.resolveToken();
        cartService.addCartItem(cartItemAddRequestDto, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    // 장바구니 수정
    @Operation(summary = "장바구니 수정")
    @PatchMapping("/cart")
    public ResponseEntity<JsonResponse> updateCart(@RequestBody CartItemUpdateRequestDto cartItemUpdateRequestDto) {
        Long userIdx = jwtTokenProvider.resolveToken();
        cartService.updateCartItem(cartItemUpdateRequestDto, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    // 장바구니 상품 삭제
    @Operation(summary = "장바구니 삭제")
    @DeleteMapping("/cart")
    public ResponseEntity<JsonResponse> deleteCart(@RequestBody CartItemDeleteRequestDto cartItemDeleteRequestDto) {
        Long userIdx = jwtTokenProvider.resolveToken();
        cartService.deleteCartItem(cartItemDeleteRequestDto, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }
}
