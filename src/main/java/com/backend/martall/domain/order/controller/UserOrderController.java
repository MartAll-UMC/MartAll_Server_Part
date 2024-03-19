package com.backend.martall.domain.order.controller;

import com.backend.martall.domain.item.dto.ItemKeywordSearchResponseDto;
import com.backend.martall.domain.order.dto.OrderCreateRequestDto;
import com.backend.martall.domain.order.dto.OrderInquiryResponseDto;
import com.backend.martall.domain.order.service.UserOrderService;
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

@Tag(name = "Order", description = "Order API")
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserOrderController {
    private final UserOrderService userOrderService;
    private final JwtTokenProvider jwtTokenProvider;

    // 주문 생성
    @Operation(summary = "주문 생성")
    @PostMapping("/order")
    public ResponseEntity<JsonResponse> createOrder(@RequestBody OrderCreateRequestDto orderCreateRequestDto) {
        Long userIdx = jwtTokenProvider.resolveToken();
        userOrderService.createOrder(orderCreateRequestDto, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }


    // 주문 조회
    @Operation(summary = "주문 조회")
    @ApiResponse(responseCode = "200", description = "주문 조회",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = OrderInquiryResponseDto.class))))
    @GetMapping("/order")
    public ResponseEntity<JsonResponse> inquiryOrder() {
        Long userIdx = jwtTokenProvider.resolveToken();
        OrderInquiryResponseDto orderInquiryResponseDto = userOrderService.getOrder(userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, orderInquiryResponseDto));
    }

}
