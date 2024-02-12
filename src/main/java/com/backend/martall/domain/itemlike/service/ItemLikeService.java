package com.backend.martall.domain.itemlike.service;

import com.backend.martall.domain.itemlike.dto.ItemLikeInquiryResponse;
import com.backend.martall.domain.itemlike.dto.ItemLikeResponse;
import com.backend.martall.domain.itemlike.entity.ItemLike;
import com.backend.martall.domain.itemlike.repository.ItemLikeRepository;
import com.backend.martall.domain.user.entity.User;
import com.backend.martall.domain.user.entity.UserRepository;
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
    private final UserRepository userRepository;

    // 찜한 상품 조회
    public ItemLikeInquiryResponse inquiryItemLike(Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx).get();

        // userIdx의 찜한 상품 목록 불러오기
        List<ItemLike> itemLikeList = itemLikeRepository.findByUser(user);

        // 찜한 상품이 없으면 예외처리
        if(itemLikeList.isEmpty()) {
            log.info("찜한 상품이 존재하지 않음, userIdx = {}", userIdx);
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

        log.info("찜한 상품 조회, userIdx = {}", userIdx);

        return ItemLikeInquiryResponse.builder()
                .item(itemLikeResponseList)
                .build();
    }

    // 상품 찜하기
    @Transactional
    public void addItemLike(int itemId, Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx).get();

        // 이미 찜 목록에 존재하면 에러
        if(itemLikeRepository.existsByUserAndMartItemId(user, itemId)) {
            log.info("이미 찜한 상품, userIdx = {}, itemId = {}", userIdx, itemId);
            throw new BadRequestException(ITEMLIKE_ALREADY_LIKE);
        }

        // itemId가 존재하는지 확인하고 없으면 에러
//        if() {
//
//        }

        // userIdx는 나중에 추가
        ItemLike itemLike = ItemLike.builder()
                .martItemId(itemId)
                .user(user)
                .build();

        log.info("상품 찜하기, userIdx = {}, itemId = {}", 1, itemId);

        itemLikeRepository.save(itemLike);
    }

    // 상품 찜하기 취소
    @Transactional
    public void removeItemLike(int itemId, Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx).get();

        // 찜 목록에 상품이 존재하지 않으면 에러
        if(!itemLikeRepository.existsByUserAndMartItemId(user, itemId)) {
            log.info("찜이 되어 있지 않은 상품을 취소, userIdx = {}, itemId = {}", userIdx, itemId);
            throw new BadRequestException(ITEMLIKE_ALREADY_DISLIKE);
        }


        itemLikeRepository.deleteByUserAndMartItemId(user, itemId);
        log.info("상품 찜 취소, userIdx = {}, itemId = {}", userIdx, itemId);

    }

}
