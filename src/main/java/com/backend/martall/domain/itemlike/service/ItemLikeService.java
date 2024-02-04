package com.backend.martall.domain.itemlike.service;

import com.backend.martall.domain.itemlike.dto.ItemLikeInquiryResponse;
import com.backend.martall.domain.itemlike.dto.ItemLikeResponse;
import com.backend.martall.domain.itemlike.entity.ItemLike;
import com.backend.martall.domain.itemlike.repository.ItemLikeRepository;
import com.backend.martall.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.backend.martall.global.exception.ResponseStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemLikeService {

    private final ItemLikeRepository itemLikeRepository;

    // 찜한 상품 조회
    public ItemLikeInquiryResponse inquiryItemLike() {

        // userIdx의 찜한 상품 목록 불러오기
        List<ItemLike> itemLikeList = itemLikeRepository.findByUserIdx(1L);

        // 찜한 상품이 없으면 예외처리
        if(itemLikeList.isEmpty()) {
            log.info("찜한 상품이 존재하지 않음, userIdx = {}", 1);
            throw new BadRequestException(ITEMLIKE_NOT_EXIST);
        }

        // entity -> DTO
        // 상품, 마트 에서 데이터 불러오기
        List<ItemLikeResponse> itemLikeResponseList = itemLikeList.stream()
                .map(itemLike -> {
                    ItemLikeResponse itemLikeResponse = ItemLikeResponse.builder()
                            .itemId(itemLike.getMartItemId())
                            // 상품 정보에서 불러오기
                            .picName("index1 사진경로")
                            .itemName("바나나")
                            .price(5500)
                            // 마트 정보에서 불러오기
                            .martShopId(1234567890L)
                            .martName("제로마트")
                            .build();
                    return itemLikeResponse;
                })
                .collect(Collectors.toList());

        log.info("찜한 상품 조회, userIdx = {}", 1);

        return ItemLikeInquiryResponse.builder()
                .item(itemLikeResponseList)
                .build();
    }

    // 상품 찜하기
    @Transactional
    public void addItemLike(int itemId) {

        // 이미 찜 목록에 존재하면 에러
        if(itemLikeRepository.existsByUserIdxAndMartItemId(1L, itemId)) {
            log.info("이미 찜한 상품, userIdx = {}, itemId = {}", 1, itemId);
            throw new BadRequestException(ITEMLIKE_ALREADY_LIKE);
        }

        // itemId가 존재하는지 확인하고 없으면 에러
//        if() {
//
//        }

        // userIdx는 나중에 추가
        ItemLike itemLike = ItemLike.builder()
                .martItemId(itemId)
                .userIdx(1L)
                .build();

        log.info("상품 찜하기, userIdx = {}, itemId = {}", 1, itemId);

        itemLikeRepository.save(itemLike);
    }

    // 상품 찜하기 취소
    @Transactional
    public void removeItemLike(int itemId) {

        // 찜 목록에 상품이 존재하지 않으면 에러
        if(!itemLikeRepository.existsByUserIdxAndMartItemId(1L, itemId)) {
            log.info("찜이 되어 있지 않은 상품을 취소, userIdx = {}, itemId = {}", 1, itemId);
            throw new BadRequestException(ITEMLIKE_ALREADY_DISLIKE);
        }

        log.info("상품 찜 취소, userIdx = {}, itemId = {}", 1, itemId);
        // userIdx는 나중에 추가
        itemLikeRepository.deleteByUserIdxAndMartItemId(1L, itemId);
    }

}
