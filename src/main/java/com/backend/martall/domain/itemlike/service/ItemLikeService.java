package com.backend.martall.domain.itemlike.service;

import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.item.repository.ItemRepository;
import com.backend.martall.domain.itemlike.dto.ItemLikeInquiryResponse;
import com.backend.martall.domain.itemlike.entity.ItemLike;
import com.backend.martall.domain.itemlike.repository.ItemLikeRepository;
import com.backend.martall.domain.mart.entity.MartShop;
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
    private final ItemRepository itemRepository;

    // 찜한 상품 조회
    public List<ItemLikeInquiryResponse> inquiryItemLike(Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx).get();

        // userIdx의 찜한 상품 목록 불러오기
        List<ItemLike> itemLikeList = itemLikeRepository.findByUser(user);

//        // 찜한 상품이 없으면 예외처리
//        if(itemLikeList.isEmpty()) {
//            log.info("찜한 상품이 존재하지 않음, userIdx = {}", userIdx);
//            throw new BadRequestException(ITEMLIKE_NOT_EXIST);
//        }

        // entity -> DTO
        // 상품, 마트 에서 데이터 불러오기
        List<ItemLikeInquiryResponse> itemLikeInquiryResponseList = itemLikeList.stream()
                .map(itemLike -> {
                    Item item  = itemLike.getItem();
                    MartShop martShop = item.getMartShop();
                    ItemLikeInquiryResponse itemLikeInquiryResponse = ItemLikeInquiryResponse.builder()
                            // 상품 정보에서 불러오기
                            .itemId(item.getItemId())
                            .itemImg(item.getProfilePhoto())
                            .itemName(item.getItemName())
                            .itemPrice(item.getPrice())
                            .itemLike(checkItemLike(item, user))
                            // 마트 정보에서 불러오기
                            .mart(ItemLikeInquiryResponse.Mart.builder()
                                    .martId(martShop.getMartShopId())
                                    .martName(martShop.getName())
                                    .build())
                            .build();
                    return itemLikeInquiryResponse;
                })
                .collect(Collectors.toList());

        log.info("찜한 상품 조회, userIdx = {}", userIdx);

        return itemLikeInquiryResponseList;
    }

    // 상품 찜하기
    @Transactional
    public void addItemLike(int itemId, Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx).get();

        // 해당하는 상품이 존재하지 않으면 예외
        Item item;
        try {
            item = itemRepository.findById(itemId).get();
        } catch (RuntimeException e) {
            throw new BadRequestException(ITEMLIKE_ITEM_NOT_EXIST);
        }


        // 이미 찜 목록에 존재하면 에러
        if(itemLikeRepository.existsByUserAndItem(user, item)) {
            log.info("이미 찜한 상품, userIdx = {}, itemId = {}", userIdx, itemId);
            throw new BadRequestException(ITEMLIKE_ALREADY_LIKE);
        }


        // userIdx는 나중에 추가
        ItemLike itemLike = ItemLike.builder()
                .item(item)
                .user(user)
                .build();

        log.info("상품 찜하기, userIdx = {}, itemId = {}", 1, itemId);

        itemLikeRepository.save(itemLike);
        item.addLike(itemLike);
    }

    // 상품 찜하기 취소
    @Transactional
    public void removeItemLike(int itemId, Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx).get();

        // 해당하는 상품이 존재하지 않으면 예외
        Item item;
        try {
            item = itemRepository.findById(itemId).get();
        } catch (RuntimeException e) {
            throw new BadRequestException(ITEMLIKE_ITEM_NOT_EXIST);
        }

        // 찜 목록에 상품이 존재하지 않으면 에러
        if(!itemLikeRepository.existsByUserAndItem(user, item)) {
            log.info("찜이 되어 있지 않은 상품을 취소, userIdx = {}, itemId = {}", userIdx, itemId);
            throw new BadRequestException(ITEMLIKE_ALREADY_DISLIKE);
        }

        ItemLike itemLike = itemLikeRepository.findByUserAndItem(user, item).get();
        item.deleteLike(itemLike);
        itemLikeRepository.delete(itemLike);
        log.info("상품 찜 취소, userIdx = {}, itemId = {}", userIdx, itemId);

    }

    // 마트에 있는 상품의 찜하기 수를 count
    @Transactional
    public int countItemLikeByMart(MartShop martShop) {
        return itemLikeRepository.countItemLikeByMart(martShop);
    }

    // 회원이 상품을 좋아요 했는지 안했는지 반환
    @Transactional
    public boolean checkItemLike(Item item, User user) {
        boolean itemLikeYN = itemLikeRepository.existsByUserAndItem(user, item);
        return itemLikeYN;
    }
}