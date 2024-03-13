package com.backend.martall.domain.mart.controller;

import com.backend.martall.domain.mart.dto.FollowedMartResponseDto;
import com.backend.martall.domain.mart.service.MartBookmarkService;
import com.backend.martall.domain.user.jwt.JwtTokenProvider;
import com.backend.martall.global.dto.JsonResponse;
import com.backend.martall.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mart/shops")
@RequiredArgsConstructor
public class MartBookmarkController {
    private JwtTokenProvider jwtTokenProvider;
    private MartBookmarkService martBookmartService;

    // 단골 마트 팔로우
    @PostMapping("/{shopId}/follow")
    public ResponseEntity<JsonResponse> followMart(@PathVariable Long shopId) {

        Long userIdx = jwtTokenProvider.resolveToken();
        martBookmartService.followMart(userIdx, shopId);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, "단골 마트로 추가되었습니다."));

    }

    // 단골 마트 팔로우 취소
    @DeleteMapping("/{shopId}/unfollow")
    public ResponseEntity<JsonResponse> unfollowMart(@PathVariable Long shopId) {
        Long userIdx = jwtTokenProvider.resolveToken();
        martBookmartService.unfollowMart(userIdx, shopId);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, "단골 마트에서 제거되었습니다."));
    }

    // 단골 마트 내역 조회
    @GetMapping("/follows")
    public ResponseEntity<JsonResponse> getFollowedMarts() {
        Long userIdx = jwtTokenProvider.resolveToken();
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, martBookmartService.getFollowedMarts(userIdx)));
    }
}
