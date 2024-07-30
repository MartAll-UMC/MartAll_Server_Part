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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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
    public ResponseEntity<JsonResponse<OwnerDto.OrderListDto>> inquiryOrder(@RequestParam String state) {
        Long userIdx = jwtTokenProvider.resolveToken();
        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, ownerService.getOrderList(state, userIdx)));
    }
}
