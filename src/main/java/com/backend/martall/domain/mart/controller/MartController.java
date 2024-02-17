package com.backend.martall.domain.mart.controller;

import java.util.List;
import com.backend.martall.domain.mart.dto.MartRequestDto;
import com.backend.martall.domain.mart.dto.MartResponseDto;
import com.backend.martall.domain.mart.service.MartService;
import com.backend.martall.global.dto.JsonResponse;
import com.backend.martall.domain.user.jwt.JwtTokenProvider;
import com.backend.martall.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




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
            try {
                Long userIdx = jwtTokenProvider.resolveToken();
                martService.followMart(shopId, userIdx);
                return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, "단골 마트로 추가되었습니다."));
            } catch (Exception e) {
                return ResponseEntity.ok(new JsonResponse(ResponseStatus.SERVER_ERROR, e.getMessage()));
            }
        }

        // 단골 마트 팔로우 취소
        @DeleteMapping("/{shopId}/unfollow")
        public ResponseEntity<JsonResponse> unfollowMart(@PathVariable Long shopId) {
            try {
                Long userIdx = jwtTokenProvider.resolveToken();
                martService.unfollowMart(shopId, userIdx);
                return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, "단골 마트에서 제거되었습니다."));
            } catch (Exception e) {
                return ResponseEntity.ok(new JsonResponse(ResponseStatus.SERVER_ERROR, e.getMessage()));
            }
        }

        // 단골 마트 내역 조회
        @GetMapping("/follows")
        public ResponseEntity<JsonResponse> getFollowedMarts() {
            try {
                Long userIdx = jwtTokenProvider.resolveToken();
                List<MartResponseDto> followedMarts = martService.getFollowedMarts(userIdx);
                return ResponseEntity.ok(new JsonResponse(ResponseStatus.SUCCESS, followedMarts));
            } catch (Exception e) {
                return ResponseEntity.ok(new JsonResponse(ResponseStatus.SERVER_ERROR, e.getMessage()));
            }
        }



        // 마트 검색
        @GetMapping("/search")
        public ResponseEntity<List<MartResponseDto>> searchMarts(@RequestParam String keyword) {
            Long userIdx = jwtTokenProvider.resolveToken();
            List<MartResponseDto> responseDtos = martService.searchMarts(keyword, userIdx);
            return ResponseEntity.ok(responseDtos);
        }

        // 마트 상세 정보 조회
        @GetMapping("/{shopId}/detail")
        public ResponseEntity<MartResponseDto> getMartDetail(@PathVariable Long shopId) {
            MartResponseDto responseDto = martService.getMartDetail(shopId);
            return ResponseEntity.ok(responseDto);
        }



        //마트 검색 by filter
        @GetMapping("/search/filter")
        public ResponseEntity<List<MartResponseDto>> searchMartsWithFilters(
                @RequestParam(required = false) String category,
                @RequestParam(required = false) Double rating) {
            List<MartResponseDto> responseDtos = martService.searchMartsByCategoryAndRating(category, rating);
            return ResponseEntity.ok(responseDtos);
        }

        //전체조회
        @GetMapping("/all")
        public ResponseEntity<JsonResponse<List<MartResponseDto>>> getAllMarts() {
            try {
                List<MartResponseDto> marts = martService.findAllMarts();
                return ResponseEntity.ok(new JsonResponse<>(ResponseStatus.SUCCESS, marts));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body(new JsonResponse<>(ResponseStatus.SERVER_ERROR, null));
            }
        }
    }

