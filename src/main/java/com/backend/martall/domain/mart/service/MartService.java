package com.backend.martall.domain.mart.service;

import com.backend.martall.domain.mart.dto.MartRequestDto;
import com.backend.martall.domain.mart.dto.MartResponseDto;
import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.mart.repository.MartRepository;
import com.backend.martall.global.exception.BadRequestException;
import com.backend.martall.global.exception.ResponseStatus;
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

    // 마트샵 생성
    @Transactional
    public MartResponseDto createMart(MartRequestDto requestDto) {
        try {
            MartShop martShop = requestDto.toEntity();
            MartShop savedMartShop = martRepository.save(martShop);
            return MartResponseDto.from(savedMartShop);
        } catch (Exception e) {
            // 마트 생성 중 오류 발생 시
            throw new BadRequestException(ResponseStatus.MART_CREATE_FAIL);
        }
    }

    // 마트 정보 업데이트
    @Transactional
    public MartResponseDto updateMart(Long shopId, MartRequestDto requestDto) {
        MartShop martShop = martRepository.findById(shopId)
                .orElseThrow(() -> new BadRequestException(ResponseStatus.MART_NAME_NOT_FOUND));
        martShop.setName(requestDto.getName());
        martShop.setIntroduction(requestDto.getIntroduction());
        martShop.setAddress(requestDto.getAddress());
        martShop.setOperatingTime(requestDto.getOperatingTime());
        martShop.setPickupTime(requestDto.getPickupTime());
        martShop.setVisitor(requestDto.getVisitor());
        martShop.setSale(requestDto.getSale());
        martShop.setProfilePhoto(requestDto.getProfilePhoto());
        martShop.setUserIdx(requestDto.getUserIdx());
        martShop.setMartCategoryId(requestDto.getMartCategoryId());
        martShop.setManagerName(requestDto.getManagerName());
        martShop.setShopNumber(requestDto.getShopNumber());
        martShop.setLinkKakao(requestDto.getLinkKakao());
        martShop.setLinkNaver(requestDto.getLinkNaver());
        martShop.setLongitude(requestDto.getLongitude());
        martShop.setLatitude(requestDto.getLatitude());

        MartShop updatedMartShop = martRepository.save(martShop);
        return MartResponseDto.from(updatedMartShop);
    }

    // 마트샵 검색 결과 -> List<MartResponseDto>
    public List<MartResponseDto> searchMarts(String keyword) {
        List<MartShop> martShops = martRepository.findByKeyword(keyword);
        if (martShops.isEmpty()) {
            // 검색 결과가 없을 경우
            throw new BadRequestException(ResponseStatus.MART_NAME_NOT_FOUND);
        }
        return martShops.stream()
                .map(MartResponseDto::from)
                .collect(Collectors.toList());
    }

    // shopId로 마트샵의 상세정보 조회
    public MartResponseDto getMartDetail(Long shopId) {
        MartShop martShop = martRepository.findById(shopId)
                .orElseThrow(() -> new BadRequestException(ResponseStatus.MART_DETAIL_FAIL));
        return MartResponseDto.from(martShop);
    }

    // rating, category filter - Martshop
    public List<MartResponseDto> searchMartsByCategoryAndRating(String category, Double rating) {
        List<MartShop> martShops = martRepository.findByCategoryAndRating(category, rating);
        if (martShops.isEmpty()) {
            // 필터 결과가 없을 경우
            throw new BadRequestException(ResponseStatus.MART_FILTER_NOT_FOUND);
        }
        return martShops.stream()
                .map(MartResponseDto::from)
                .collect(Collectors.toList());
    }
}
