package com.backend.martall.domain.mart.service;

import com.backend.martall.domain.mart.dto.MartRequestDto;
import com.backend.martall.domain.mart.dto.MartResponseDto;
import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.mart.repository.MartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MartService {

    private final MartRepository martRepository;

    public MartService(MartRepository martRepository) {
        this.martRepository = martRepository;
    }
//마트샵 생성
    @Transactional
    public MartResponseDto createMart(MartRequestDto requestDto) {
        MartShop martShop = requestDto.toEntity();
        MartShop savedMartShop = martRepository.save(martShop);
        return MartResponseDto.from(savedMartShop);
    }

    @Transactional
    public MartResponseDto updateMart(Long shopId, MartRequestDto requestDto) {
        MartShop martShop = martRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("MartShop not found"));
        return MartResponseDto.from(martShop);
    }
//마트샵 검색 결과-> List<MartResponseDto?
    public List<MartResponseDto> searchMarts(String keyword) {
        List<MartShop> martShops = martRepository.findByKeyword(keyword);
        return martShops.stream()
                .map(MartResponseDto::from)
                .collect(Collectors.toList());
    }
//shopId로 마트샵의 상세정보 조회
    public MartResponseDto getMartDetail(Long shopId) {
        MartShop martShop = martRepository.findById(shopId)
                .orElseThrow(() -> new RuntimeException("MartShop not found"));
        return MartResponseDto.from(martShop);
    }

    //rating, category filter - Martshop

    public List<MartResponseDto> searchMartsByCategoryAndRating(String category, Double rating) {
        List<MartShop> martShops = martRepository.findByCategoryAndRating(category, rating);
        return martShops.stream()
                .map(MartResponseDto::from)
                .collect(Collectors.toList());
    }

}
