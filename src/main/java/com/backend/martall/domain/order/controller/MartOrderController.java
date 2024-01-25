package com.backend.martall.domain.order.controller;

import com.backend.martall.domain.order.service.MartOrderService;
import com.backend.martall.global.dto.JsonResponse;
import com.backend.martall.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mart")
@RequiredArgsConstructor
public class MartOrderController {
    private final MartOrderService martOrderService;

    @PatchMapping("/order/{orderId}/confirm")
    public ResponseEntity<JsonResponse> confirmOrder(@PathVariable Long orderId){
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @PatchMapping("/order/{orderId}/complete")
    public ResponseEntity<JsonResponse> completeOrder(@PathVariable Long orderId){
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }

    @PatchMapping("/order/{orderId}/cancel")
    public ResponseEntity<JsonResponse> cancelOrder(@PathVariable Long orderId){
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, null));
    }
}
