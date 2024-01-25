package com.backend.martall.domain.order.controller;

import com.backend.martall.domain.order.service.UserOrderService;
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

    @PostMapping("/order")
    public ResponseEntity<JsonResponse> createOrder(){
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @GetMapping("/order")
    public ResponseEntity<JsonResponse> inquiryOrder(@RequestParam String filter){
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<JsonResponse> inquiryOrderDetail(@PathVariable Long orderId){
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @PatchMapping("/order/{orderId}/cancel")
    public ResponseEntity<JsonResponse> cancelOrder(@PathVariable Long orderId){
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }
}
