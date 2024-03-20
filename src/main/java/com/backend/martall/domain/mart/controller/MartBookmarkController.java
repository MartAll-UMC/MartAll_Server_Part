package com.backend.martall.domain.mart.controller;

import com.backend.martall.domain.mart.dto.MartDetailResponseDto;
import com.backend.martall.domain.mart.dto.MartResponseDto;
import com.backend.martall.domain.mart.service.MartBookmarkService;
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

import java.util.List;

@Tag(name="MartBookmark", description = "MartBookmark API")
@RestController
@RequestMapping("/mart/shops")
@RequiredArgsConstructor
public class MartBookmarkController {
    private final JwtTokenProvider jwtTokenProvider;
    private final MartBookmarkService martBookmarkService;

    // 단골 마트 팔로우
    @Operation(summary = "단골 마트 등록")
    @Parameter(name = "shopId", description = "마트의 아이디")
    @PostMapping("/{shopId}/follow")
    public ResponseEntity<JsonResponse> followMart(@PathVariable Long shopId) {

        Long userIdx = jwtTokenProvider.resolveToken();
        martBookmarkService.followMart(userIdx, shopId);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, "단골 마트로 추가되었습니다."));

    }

    // 단골 마트 팔로우 취소
    @Operation(summary = "단골 마트 등록 취소")
    @Parameter(name = "shopId", description = "마트의 아이디")
    @DeleteMapping("/{shopId}/unfollow")
    public ResponseEntity<JsonResponse> unfollowMart(@PathVariable Long shopId) {
        Long userIdx = jwtTokenProvider.resolveToken();
        martBookmarkService.unfollowMart(userIdx, shopId);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, "단골 마트에서 제거되었습니다."));
    }

    // 단골 마트 내역 조회
    @Operation(summary = "단골 마트 목록")
    @ApiResponse(responseCode = "200", description = "단골 마트 목록",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = MartResponseDto.class) )))
    @GetMapping("/follows")
    public ResponseEntity<JsonResponse> getFollowedMarts() {
        Long userIdx = jwtTokenProvider.resolveToken();
        List<MartResponseDto> martResponseDtoList = martBookmarkService.getFollowedMarts(userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, martResponseDtoList));
    }
}
