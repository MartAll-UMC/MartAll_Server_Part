package com.backend.martall.domain.order.controller;

import com.backend.martall.domain.order.dto.OrderCreateRequestDto;
import com.backend.martall.domain.order.service.UserOrderService;
import com.backend.martall.domain.user.jwt.JwtTokenProvider;
import com.backend.martall.global.dto.JsonResponse;
import com.backend.martall.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserOrderController {
    private final UserOrderService userOrderService;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/order")
    public ResponseEntity<JsonResponse> createOrder(@RequestBody OrderCreateRequestDto orderCreateRequestDto) {
        Long userIdx = jwtTokenProvider.resolveToken();
        userOrderService.createOrder(orderCreateRequestDto, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @GetMapping("/order")
    public ResponseEntity<JsonResponse> inquiryOrder() {
        Long userIdx = jwtTokenProvider.resolveToken();
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, userOrderService.getOrder(userIdx)));
    }

}
