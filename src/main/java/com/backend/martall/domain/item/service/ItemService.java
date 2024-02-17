package com.backend.martall.domain.item.service;

import com.backend.martall.domain.image.dto.ImageDto;
import com.backend.martall.domain.image.service.ImageService;
import com.backend.martall.domain.item.dto.*;
import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.item.entity.ItemCategory;
import com.backend.martall.domain.item.entity.ItemPic;
import com.backend.martall.domain.item.repository.ItemPicRepository;
import com.backend.martall.domain.item.repository.ItemRepository;
import com.backend.martall.domain.itemlike.service.ItemLikeService;
import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.mart.repository.MartRepository;
import com.backend.martall.domain.user.entity.User;
import com.backend.martall.domain.user.entity.UserRepository;
import com.backend.martall.global.exception.BadRequestException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

import static com.backend.martall.global.exception.ResponseStatus.*;

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
    private final UserRepository userRepository;
    private final ItemLikeService itemLikeService;
    private final ImageService imageService;

    public List<ItemListResponseDto> searchItems(String itemName, Long userIdx) {

        // 검색어가 공백일 경우 예외
        if (!StringUtils.hasText(itemName)) {
            throw new BadRequestException(ITEM_SEARCH_WORD_EMPTY);
        }

        List<Item> items = itemRepository.searchByItemName(itemName);

        // 검색 결과가 없으면 예외처리
        if (items.isEmpty()) {
            throw new BadRequestException(ITEM_SEARCH_EMPTY);
        }

        User user = userRepository.findByUserIdx(userIdx).get();

        // item을 DTO에 맞게 가공
        List<ItemListResponseDto> itemListResponseDtos = items.stream()
                .map(item -> {
                    ItemListResponseDto itemListResponseDto = ItemListResponseDto.from(item);
                    itemListResponseDto.setLike(itemLikeService.checkItemLike(item, user));
                    return itemListResponseDto;
                })
                .collect(Collectors.toList());

        return itemListResponseDtos;
    }

    public ItemDetailResponseDto getItemDetail(Long shopId, int itemId, Long userIdx) {

        // 상품 아이디에 해당하는 상품이 없으면 예외
        Item item;
        try {
            item = itemRepository.findByItemId(itemId).get();
        } catch (RuntimeException e) {
            throw new BadRequestException(ITEM_DETAIL_ITEMID_FAIL);
        }

        // 마트 아이디가 상품의 마트의 아이디와 같지 않으면 예외
        MartShop martShop = item.getMartShop();
        if (!martShop.getMartShopId().equals(shopId)) {
            throw new BadRequestException(ITEM_DETAIL_MARTID_FAIL);
        }

        // 반환 할 DTO 에 상품 정보 주입
        User user = userRepository.findByUserIdx(userIdx).get();
        ItemDetailResponseDto itemDetailResponseDto = ItemDetailResponseDto.from(item);
        itemDetailResponseDto.setLike(itemLikeService.checkItemLike(item, user));

        // 마트 정보 주입
        ItemMartShopResponseDto itemMartShopResponseDto = ItemMartShopResponseDto.builder()
                .martShopId(martShop.getMartShopId())
                .martName(martShop.getName())
                .bookmarkCount(martShop.getMartBookmarks().size())
                .likeCount(itemLikeService.countItemLikeByMart(martShop))
                .build();

        itemDetailResponseDto.setMart(itemMartShopResponseDto);

        return itemDetailResponseDto;
    }


    public List<ItemNewResponseDto> newItems(Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx).get();

        // 시간 기준 내림차순으로 8개의 상품 불러오기
        List<Item> items = itemRepository.findTop8ByOrderByCreatedAtDesc();

        List<ItemNewResponseDto> itemNewResponseDtos = items.stream()
                .map(item -> {
                    ItemNewResponseDto itemNewResponseDto = ItemNewResponseDto.from(item);
                    itemNewResponseDto.setLike(itemLikeService.checkItemLike(item, user));
                    return itemNewResponseDto;
                })
                .collect(Collectors.toList());

        return itemNewResponseDtos;
    }

    public List<ItemCategoryResponseDto> getCategoryItem(String category, Integer minPrice, Integer maxPrice, String sort, Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx).get();

        ItemCategory itemCategory = ItemCategory.findByName(category);

        List<Item> itemList;

        if(sort.equals("기본")) {
            itemList = itemRepository.searchByCategoryAndPrice(itemCategory, minPrice, maxPrice);
        } else if(sort.equals("최신")) {
            itemList = itemRepository.searchByCategoryAndPriceCreatedAtDesc(itemCategory, minPrice, maxPrice);
        } else if(sort.equals("찜")) {
            itemList = itemRepository.searchByCategoryAndPriceLikeDesc(itemCategory, minPrice, maxPrice);
        } else {
            throw new BadRequestException(ITEM_CATEGORY_SORT_WRONG);
        }

        List<ItemCategoryResponseDto> itemCategoryResponseDtoList = itemList.stream()
                .map(item -> {
                    ItemCategoryResponseDto itemCategoryResponseDto = ItemCategoryResponseDto.from(item);
                    itemCategoryResponseDto.setLike(itemLikeService.checkItemLike(item, user));
                    return itemCategoryResponseDto;
                })
                .collect(Collectors.toList());

        return itemCategoryResponseDtoList;
    }

    public List<ItemMartNewResponseDto> getMartNewItem(MartShop martShop, User user) {
        List<Item> itemList = itemRepository.findByMartShopOrderByCreatedAtDesc(martShop);

        List<ItemMartNewResponseDto> itemMartNewResponseDtoList = itemList.stream()
                .map(item -> {
                    ItemMartNewResponseDto itemMartNewResponseDto = ItemMartNewResponseDto.of(item);
                    itemMartNewResponseDto.setLikeYn(itemLikeService.checkItemLike(item, user));
                    return itemMartNewResponseDto;
                })
                .collect(Collectors.toList());

        return itemMartNewResponseDtoList;
    }

    // 테스트 코드
    @Transactional
    public void addItem(ItemAddRequestDto itemAddRequestDto) {
        MartShop martShop = martRepository.findById(itemAddRequestDto.getMartShopId()).get();

        Item item = itemAddRequestDto.toEntity(martShop);

        itemRepository.save(item);
    }

    @Transactional
    public void addItemWithImage(MultipartFile profileImage, MultipartFile contentImage, ItemAddRequestDto itemAddRequestDto) {
        MartShop martShop = martRepository.findById(itemAddRequestDto.getMartShopId()).get();

        Item item = itemAddRequestDto.toEntity(martShop);

        ImageDto.ImageRequest imageRequest = new ImageDto.ImageRequest("item", (long) item.getItemId());
        String profileUrl = imageService.createImage(profileImage, imageRequest);
        String contentUrl = imageService.createImage(contentImage, imageRequest);

        item.setPic(profileUrl, contentUrl);

        itemRepository.save(item);
    }
}
