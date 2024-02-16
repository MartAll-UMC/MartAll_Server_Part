package com.backend.martall.domain.mart.controller;


import java.util.List;

import com.backend.martall.domain.mart.dto.MartSearchResponseDto;
import com.backend.martall.domain.mart.dto.MartRequestDto;
import com.backend.martall.domain.mart.dto.MartResponseDto;
import com.backend.martall.domain.mart.service.MartService;
import com.backend.martall.global.dto.JsonResponse;
import com.backend.martall.domain.user.jwt.JwtTokenProvider;
import com.backend.martall.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.backend.martall.domain.mart.dto.AllMartResponseDto;
import com.backend.martall.domain.mart.dto.FollowedMartResponseDto;


@RestController
@RequestMapping("/mart/shops")
@RequiredArgsConstructor
public class MartController {

    private final MartService martService;
    private final JwtTokenProvider jwtTokenProvider;

    // 마트 생성
    @PostMapping
    public ResponseEntity<JsonResponse> createMart(@RequestBody MartRequestDto requestDto) {
        try {
            Long userIdx = jwtTokenProvider.resolveToken();
            MartResponseDto responseDto = martService.createMart(requestDto, userIdx);
            return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, responseDto));
        } catch (Exception e) {
            return ResponseEntity.ok(new JsonResponse(ResponseStatus.SERVER_ERROR, e.getMessage()));
        }
    }

    // 마트 정보 업데이트
    @PatchMapping("/{shopId}")
    public ResponseEntity<JsonResponse> updateMartShop(@PathVariable Long shopId, @RequestBody MartRequestDto requestDto) {
        try {
            Long userIdx = jwtTokenProvider.resolveToken();
            MartResponseDto responseDto = martService.updateMart(shopId, requestDto, userIdx);
            return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, responseDto));
        } catch (Exception e) {
            return ResponseEntity.ok(new JsonResponse(ResponseStatus.SERVER_ERROR, e.getMessage()));
        }
    }

    // 단골 마트 팔로우
    @PostMapping("/{shopId}/follow")
    public ResponseEntity<JsonResponse> followMart(@PathVariable Long shopId) {

        Long userIdx = jwtTokenProvider.resolveToken();
        martService.followMart(userIdx, shopId);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, "단골 마트로 추가되었습니다."));

    }

    // 단골 마트 팔로우 취소
    @DeleteMapping("/{shopId}/unfollow")
    public ResponseEntity<JsonResponse> unfollowMart(@PathVariable Long shopId) {

        Long userIdx = jwtTokenProvider.resolveToken();
        martService.unfollowMart(userIdx, shopId);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, "단골 마트에서 제거되었습니다."));

    }

    // 단골 마트 내역 조회
    @GetMapping("/follows")
    public ResponseEntity<JsonResponse> getFollowedMarts() {
        Long userIdx = jwtTokenProvider.resolveToken();
        List<FollowedMartResponseDto> followedMarts = martService.getFollowedMarts(userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, followedMarts));
    }


    // 마트 검색
    @GetMapping("/search")
    public ResponseEntity<JsonResponse> searchMarts(@RequestParam String keyword) {
        Long userIdx = jwtTokenProvider.resolveToken();
        List<MartResponseDto> responseDtos = martService.searchMarts(keyword, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, responseDtos));
    }

    // 마트 상세 정보 조회
    @GetMapping("/{shopId}/detail")
    public ResponseEntity<JsonResponse> getMartDetail(@PathVariable Long shopId) {
        MartResponseDto responseDto = martService.getMartDetail(shopId);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, responseDto));
    }


    //마트 검색 by filter
    @GetMapping("/search/filter")
    public ResponseEntity<JsonResponse> searchMartsWithFilters(
            @RequestParam(defaultValue = "전체") String tag,
            @RequestParam(required = false) Integer minBookmark,
            @RequestParam(required = false) Integer maxBookmark,
            @RequestParam(required = false) Integer minLike,
            @RequestParam(required = false) Integer maxLike,
            @RequestParam(defaultValue = "기본") String sort) {
        Long userIdx = jwtTokenProvider.resolveToken();
        List<MartSearchResponseDto> responseDtos = martService.searchMartsByCategoryAndRating(tag, minBookmark, maxBookmark, minLike, maxLike, sort, userIdx);
        return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, responseDtos));
    }

    @GetMapping("/all")
    public ResponseEntity<JsonResponse> getAllMarts() {
        Long userIdx = jwtTokenProvider.resolveToken();
        List<MartSearchResponseDto> marts = martService.findAllMarts(userIdx);
        return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, marts));
    }
}