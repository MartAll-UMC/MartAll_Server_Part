package com.backend.martall.domain.owner.controller;

import com.backend.martall.domain.owner.dto.OwnerDto;
import com.backend.martall.domain.owner.service.OwnerService;
import com.backend.martall.domain.user.jwt.JwtTokenProvider;
import com.backend.martall.global.dto.JsonResponse;
import com.backend.martall.global.exception.ResponseStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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

    @Operation(summary = "상품 등록")
    @ApiResponse(responseCode = "200", description = "상품 등록", useReturnTypeSchema = true)
    @PostMapping("/item/registration")
    public ResponseEntity<JsonResponse<OwnerDto.ItemResponseDto>> createItem(@RequestPart(name = "profile") MultipartFile profile,
                                                                             @RequestPart(name = "content") MultipartFile content,
                                                                             @RequestPart(name = "dto") @Valid OwnerDto.ItemCreateRequestDto itemRequestDto) {
        Long userIdx = jwtTokenProvider.resolveToken();
        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, ownerService.registerItem(profile, content, itemRequestDto, userIdx)));
    }

    @Operation(summary = "상품 수정")
    @ApiResponse(responseCode = "200", description = "상품 수정", useReturnTypeSchema = true)
    @PatchMapping("/item/update")
    public ResponseEntity<JsonResponse<OwnerDto.ItemResponseDto>> updateItem(@RequestPart(name = "profile", required = false) MultipartFile profile,
                                                                             @RequestPart(name = "content", required = false) MultipartFile content,
                                                                             @RequestPart(name = "dto") @Valid OwnerDto.ItemUpdateRequestDto itemRequestDto) {
        Long userIdx = jwtTokenProvider.resolveToken();
        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, ownerService.updateItem(profile, content, itemRequestDto, userIdx)));
    }

    @Operation(summary = "가게 노출 상태 수정")
    @ApiResponse(responseCode = "200", description = "가게 노출 상태 수정", useReturnTypeSchema = true)
    @PatchMapping("/shops/change-state")
    public ResponseEntity<JsonResponse<OwnerDto.MartExposureResponseDto>> updateMartExposure(@RequestBody OwnerDto.MartExposureRequestDto martExposureRequestDto) {
        Long userIdx = jwtTokenProvider.resolveToken();
        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, ownerService.updateMartExposure(martExposureRequestDto, userIdx)));
    }

    @Operation(summary = "홈 화면")
    @ApiResponse(responseCode = "200", description = "홈 화면", useReturnTypeSchema = true)
    @GetMapping("/shops/all")
    public ResponseEntity<JsonResponse<OwnerDto.MartMainResponseDto>> home() {
        Long userIdx = jwtTokenProvider.resolveToken();
        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, ownerService.getMartMain(userIdx)));
    }
}
