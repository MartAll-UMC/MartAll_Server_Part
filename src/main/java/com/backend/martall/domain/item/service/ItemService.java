package com.backend.martall.domain.item.service;

import com.backend.martall.domain.item.dto.*;
import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.item.entity.ItemPic;
import com.backend.martall.domain.item.repository.ItemPicRepository;
import com.backend.martall.domain.item.repository.ItemRepository;
import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.mart.repository.MartRepository;
import com.backend.martall.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.backend.martall.global.exception.ResponseStatus.ITEM_DETAIL_FAIL;

@Service
@RequiredArgsConstructor
public class ItemService {

    /*
        좋아요 여부 반영하기
        martshop likecount 로직 추가되면 반영하기
     */

    private final ItemRepository itemRepository;
    private final ItemPicRepository itemPicRepository;
    private final MartRepository martRepository;

    public List<ItemListResponseDto> searchItems(String itemName) {

        List<Item> items = itemRepository.findByItemName(itemName);

        // item을 DTO에 맞게 가공
        List<ItemListResponseDto> itemListResponseDtos = items.stream()
                .map(item -> {
                    ItemListResponseDto itemListResponseDto = ItemListResponseDto.from(item);
                    itemListResponseDto.setPic(item.getItemPicList().get(0).getPicName());
                    // item의 마트 아이디로 마트 이름을 찾는 코드
                    // 나중에 연관관계 설정하면 쉽게 찾을 수 있을 듯 해요!!
                    // --> 위의 사진과 마트 수정했습니다.
                    itemListResponseDto.setMartShopName(item.getMartShop().getName());

                    return itemListResponseDto;
                })
                .collect(Collectors.toList());

        return itemListResponseDtos;
    }

    public ItemDetailResponseDto getItemDetail(Long shopId, int itemId) {

        MartShop martShop = martRepository.findById(shopId).get();

        Item item;

        try {
            item = itemRepository.findByMartShopAndItemId(martShop, itemId).get();
        } catch (RuntimeException e) {
            throw new BadRequestException(ITEM_DETAIL_FAIL);
        }


        ItemDetailResponseDto itemDetailResponseDto = ItemDetailResponseDto.from(item);

        ItemMartShopResponseDto itemMartShopResponseDto = ItemMartShopResponseDto.builder()
                .martShopId(item.getMartShop().getMartShopId())
                // 나중에 연관관계 설정하시면 마트관련 정보를 불러와서 설정할 수 있을 것 같아요!!
                // -> 마트 연관관계에서 불러오게 수정했습니다
                // likeCount는 따로 로직이 필요할 것 같아요
                .martName(item.getMartShop().getName())
                .bookmarkCount(item.getMartShop().getMartBookmarks().size())
                .likeCount(123)
                .build();

        itemDetailResponseDto.setMart(itemMartShopResponseDto);
        return itemDetailResponseDto;
    }


    public List<ItemNewResponseDto> newItems() {
        List<Item> items = itemRepository.findTop8ByOrderByCreatedAtDesc();

        List<ItemNewResponseDto> itemNewResponseDtos = items.stream()
                .map(item -> {
                    ItemNewResponseDto itemNewResponseDto = ItemNewResponseDto.from(item);
                    // 상품 사진 중 첫번째 사진을 프로필로 등록해요!
                    // -> 이부분도 연관관계에서 불러오게 수정했습니다.
                    itemNewResponseDto.setPic(item.getItemPicList().get(0).getPicName());
                    return itemNewResponseDto;
                })
                .collect(Collectors.toList());

        return itemNewResponseDtos;
    }

    // 테스트 코드
    @Transactional
    public void addItem(ItemAddRequestDto itemAddRequestDto) {
        MartShop martShop = martRepository.findById(itemAddRequestDto.getMartShopId()).get();
        Item item = itemAddRequestDto.toEntity(martShop);
        ItemPic itemPic1 = ItemPic.builder()
                .picIndex(1)
                .picName(item.getProfilePhoto())
                .build();

        item.addPic(itemPic1);
        itemRepository.save(item);
        itemPicRepository.save(itemPic1);

    }
}
