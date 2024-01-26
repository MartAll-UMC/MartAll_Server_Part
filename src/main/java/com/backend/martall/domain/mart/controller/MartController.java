package com.backend.martall.domain.mart.controller;
import java.util.List;
import com.backend.martall.domain.mart.dto.MartRequestDto;
import com.backend.martall.domain.mart.dto.MartResponseDto;
import com.backend.martall.domain.mart.service.MartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mart/shops")
@RequiredArgsConstructor
public class MartController {

    private final MartService martService;

    // 마트 생성
    @PostMapping
    public ResponseEntity<MartResponseDto> createMart(@RequestBody MartRequestDto requestDto) {
        MartResponseDto responseDto = martService.createMart(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    // 마트 정보 업데이트
    @PatchMapping("/{shopId}")
    public ResponseEntity<MartResponseDto> updateMartShop(@PathVariable Long shopId, @RequestBody MartRequestDto requestDto) {
        MartResponseDto responseDto = martService.updateMart(shopId, requestDto);
        return ResponseEntity.ok(responseDto);
    }

    // 마트 검색
    @GetMapping("/search")
    public ResponseEntity<List<MartResponseDto>> searchMarts(@RequestParam String keyword) {
        List<MartResponseDto> responseDtos = martService.searchMarts(keyword);
        return ResponseEntity.ok(responseDtos);
    }

    // 마트 상세 정보 조회
    @GetMapping("/{shopId}")
    public ResponseEntity<MartResponseDto> getMartDetail(@PathVariable Long shopId) {
        MartResponseDto responseDto = martService.getMartDetail(shopId);
        return ResponseEntity.ok(responseDto);
    }
}
