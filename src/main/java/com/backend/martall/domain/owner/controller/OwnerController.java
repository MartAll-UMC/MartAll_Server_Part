package com.backend.martall.domain.owner.controller;

import com.backend.martall.domain.owner.dto.OwnerDto;
import com.backend.martall.domain.owner.service.OwnerService;
import com.backend.martall.domain.user.jwt.JwtTokenProvider;
import com.backend.martall.global.dto.JsonResponse;
import com.backend.martall.global.exception.ResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Owner", description = "Owner API")
@RestController
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerController {

    private final JwtTokenProvider jwtTokenProvider;
    private final OwnerService ownerService;

    @Operation(summary = "주문 내역 조회")
    @ApiResponse(responseCode = "200", description = "주문 내역 조회", useReturnTypeSchema = true)
    @GetMapping("/order/list")
    public ResponseEntity<JsonResponse<OwnerDto.OrderListResponseDto>> inquiryOrder(@RequestParam String state) {
        Long userIdx = jwtTokenProvider.resolveToken();
        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, ownerService.getOrderList(state, userIdx)));
    }

    @Operation(summary = "주문 내역 상태 변경")
    @ApiResponse(responseCode = "200", description = "주문 내역 상태 변경", useReturnTypeSchema = true)
    @PatchMapping("/order/update-state")
    public ResponseEntity<JsonResponse<OwnerDto.OrderStateUpdateResponseDto>> updateOrder(@RequestBody OwnerDto.OrderStateUpdateRequestDto orderStateUpdateRequestDto) {
        Long userIdx = jwtTokenProvider.resolveToken();
        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, ownerService.updateOrderState(orderStateUpdateRequestDto, userIdx)));
    }

    @Operation(summary = "주문 상세 내역 조회")
    @ApiResponse(responseCode = "200", description = "주문 상세 내역 조회", useReturnTypeSchema = true)
    @GetMapping("/order/check-detail")
    public ResponseEntity<JsonResponse<OwnerDto.OrderDetailResponseDto>> inquiryOrderDetail(@RequestBody OwnerDto.OrderDetailRequestDto orderDetailRequestDto) {
        Long userIdx = jwtTokenProvider.resolveToken();
        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, ownerService.getOrderDetail(orderDetailRequestDto, userIdx)));
    }
}
