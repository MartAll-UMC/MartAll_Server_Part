package com.backend.martall.domain.item.service;

import com.backend.martall.domain.item.dto.*;
import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.item.entity.ItemPic;
import com.backend.martall.domain.item.repository.ItemPicRepository;
import com.backend.martall.domain.item.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemPicRepository itemPicRepository;

    public List<ItemListResponseDto> searchItems(String itemName) {

        List<Item> items = itemRepository.findByItemName(itemName);

        // item을 DTO에 맞게 가공
        List<ItemListResponseDto> itemListResponseDtos = items.stream()
                .map(item -> {
                    ItemListResponseDto itemListResponseDto = ItemListResponseDto.from(item);
                    itemListResponseDto.setPic(itemPicRepository.findByPicItemAndPicIndex(item, 1).get().getPicName());
                    // item의 마트 아이디로 마트 이름을 찾는 코드
                    // 나중에 연관관계 설정하면 쉽게 찾을 수 있을 듯 해요!!
                    itemListResponseDto.setMartShopName("제로마트");

                    return itemListResponseDto;
                })
                .collect(Collectors.toList());

        return itemListResponseDtos;
    }

    public ItemDetailResponseDto getItemDetail(Long shopId, int itemId) {
        Item item = itemRepository.findById(shopId, itemId);

        ItemDetailResponseDto itemDetailResponseDto = ItemDetailResponseDto.from(item);

        ItemMartShopResponseDto itemMartShopResponseDto = ItemMartShopResponseDto.builder()
                .martShopId(item.getMartShopId())
                // 나중에 연관관계 설정하시면 마트관련 정보를 불러와서 설정할 수 있을 것 같아요!!
                .martName("제로마트")
                .bookmarkCount(123)
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
                    itemNewResponseDto.setPic(itemPicRepository.findByPicItemAndPicIndex(item, 1).get().getPicName());
                    return itemNewResponseDto;
                })
                .collect(Collectors.toList());

        return itemNewResponseDtos;
    }

    // 상품 하나당 사진 3개를 추가
    // 테스트 코드
    public void addItem(ItemAddRequestDto itemAddRequestDto) {
        Item item = itemAddRequestDto.toEntity();
        ItemPic itemPic1 = ItemPic.builder()
                .picItem(item)
                .picIndex(1)
                .picName("사진 경로1")
                .build();

        ItemPic itemPic2 = ItemPic.builder()
                .picItem(item)
                .picIndex(2)
                .picName("사진 경로1")
                .build();

        ItemPic itemPic3 = ItemPic.builder()
                .picItem(item)
                .picIndex(3)
                .picName("사진 경로1")
                .build();

        itemRepository.save(item);
        itemPicRepository.save(itemPic1);
        itemPicRepository.save(itemPic2);
        itemPicRepository.save(itemPic3);
    }
}
