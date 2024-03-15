package com.backend.martall.domain.item.service;

import com.backend.martall.domain.image.dto.ImageDto;
import com.backend.martall.domain.image.service.ImageService;
import com.backend.martall.domain.item.dto.*;
import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.item.entity.ItemCategory;
import com.backend.martall.domain.item.repository.ItemPicRepository;
import com.backend.martall.domain.item.repository.ItemRepository;
import com.backend.martall.domain.itemlike.service.ItemLikeService;
import com.backend.martall.domain.mart.entity.MartCategory;
import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.mart.repository.MartBookmarkRepository;
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


    private final ItemRepository itemRepository;
    private final ItemPicRepository itemPicRepository;
    private final MartRepository martRepository;
    private final UserRepository userRepository;
    private final ItemLikeService itemLikeService;
    private final ImageService imageService;
    private final MartBookmarkRepository martBookmarkRepository;

    public List<ItemKeywordSearchResponseDto> searchItems(String keyword, Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx).get();

        // 검색어가 공백일 경우 예외
        if (!StringUtils.hasText(keyword)) {
            throw new BadRequestException(ITEM_SEARCH_WORD_EMPTY);
        }

        List<Item> items = itemRepository.searchByItemName(keyword);

        // dto 리스트 생성
        List<ItemKeywordSearchResponseDto> itemKeywordSearchResponseDtoList = items.stream()
                .map(item -> {
                    ItemKeywordSearchResponseDto itemKeywordSearchResponseDto = ItemKeywordSearchResponseDto.builder()
                            .itemId(item.getItemId())
                            .itemImg(item.getProfilePhoto())
                            .itemName(item.getItemName())
                            .itemPrice(item.getPrice())
                            .isLike(itemLikeService.checkItemLike(item, user))
                            .mart(ItemKeywordSearchResponseDto.Mart.builder()
                                    .martId(item.getMartShop().getMartShopId())
                                    .martName(item.getMartShop().getName())
                                    .build())
                            .build();
                    return itemKeywordSearchResponseDto;
                })
                .collect(Collectors.toList());

        return itemKeywordSearchResponseDtoList;
    }

    // 상품 상세정보
    public ItemDetailResponseDto getItemDetail(Long shopId, int itemId, Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx).get();

        // 상품 아이디에 해당하는 상품이 없으면 예외
        Item item = itemRepository.findByItemId(itemId)
                .orElseThrow(() -> new BadRequestException(ITEM_DETAIL_ITEMID_FAIL));


        // 마트 아이디가 상품의 마트의 아이디와 같지 않으면 예외
        MartShop martShop = item.getMartShop();
        if (!martShop.getMartShopId().equals(shopId)) {
            throw new BadRequestException(ITEM_DETAIL_MARTID_FAIL);
        }

        // dto 생성
        ItemDetailResponseDto itemDetailResponseDto = ItemDetailResponseDto.builder()
                .itemId(item.getItemId())
                .itemImg(item.getProfilePhoto())
                .itemName(item.getItemName())
                .itemPrice(item.getPrice())
                .isLike(itemLikeService.checkItemLike(item, user))
                .itemContentImg(item.getContent())
                // 마트 정보
                .mart(ItemDetailResponseDto.Mart.builder()
                        .martId(martShop.getMartShopId())
                        .martName(martShop.getName())
                        .martCategory(martShop.getMartCategories().stream()
                                .map(MartCategory::getCategoryName)
                                .collect(Collectors.toList()))
                        .bookmarkCount(martShop.getMartBookmarks().size())
                        .likeCount(itemLikeService.countItemLikeByMart(martShop))
                        .isBookmark(martShop.getMartBookmarks().stream()
                                .anyMatch(bookmark -> bookmark.getUser().equals(user)))
                        .build())
                .build();


        return itemDetailResponseDto;
    }


    // 전체 새상품
    public List<ItemNewResponseDto> newItems(Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx).get();

        // 시간 기준 내림차순으로 8개의 상품 불러오기
        List<Item> itemList = itemRepository.findTop8ByOrderByCreatedAtDesc();

        // dto 생성
        List<ItemNewResponseDto> itemNewResponseDtoList = itemList.stream()
                .map(item -> {
                    MartShop martShop = item.getMartShop();
                    ItemNewResponseDto itemNewResponseDto = ItemNewResponseDto.builder()
                            .itemId(item.getItemId())
                            .itemImg(item.getProfilePhoto())
                            .itemName(item.getItemName())
                            .itemPrice(item.getPrice())
                            .isLike(itemLikeService.checkItemLike(item, user))
                            // 마트 정보
                            .mart(ItemNewResponseDto.Mart.builder()
                                    .martId(martShop.getMartShopId())
                                    .martName(martShop.getName())
                                    .build())
                            .build();
                    return itemNewResponseDto;
                })
                .collect(Collectors.toList());

        return itemNewResponseDtoList;
    }

    // 카테고리 검색
    // 페이징 적용해야됨
    public List<ItemCategorySearchResponseDto> getCategoryItem(String category, Integer minPrice, Integer maxPrice, String sort, Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx).get();

        ItemCategory itemCategory = ItemCategory.findByName(category);

        List<Item> itemList;

        if (sort.equals("기본")) {
            itemList = itemRepository.searchByCategoryAndPrice(itemCategory, minPrice, maxPrice);
        } else if (sort.equals("최신")) {
            itemList = itemRepository.searchByCategoryAndPriceCreatedAtDesc(itemCategory, minPrice, maxPrice);
        } else if (sort.equals("찜")) {
            itemList = itemRepository.searchByCategoryAndPriceLikeDesc(itemCategory, minPrice, maxPrice);
        } else {
            throw new BadRequestException(ITEM_CATEGORY_SORT_WRONG);
        }

        // dto 생성
        List<ItemCategorySearchResponseDto> itemCategorySearchResponseDtoList = itemList.stream()
                .map(item -> {
                    MartShop martShop = item.getMartShop();
                    ItemCategorySearchResponseDto itemCategorySearchResponseDto = ItemCategorySearchResponseDto.builder()
                            .itemId(item.getItemId())
                            .itemImg(item.getProfilePhoto())
                            .itemName(item.getItemName())
                            .itemPrice(item.getPrice())
                            .isLike(itemLikeService.checkItemLike(item, user))
                            // 마트 정보
                            .mart(ItemCategorySearchResponseDto.Mart.builder()
                                    .martId(martShop.getMartShopId())
                                    .martName(martShop.getName())
                                    .build())
                            .build();
                    return itemCategorySearchResponseDto;
                })
                .collect(Collectors.toList());

        return itemCategorySearchResponseDtoList;
    }

    // 마트 상품 최신순
    // 페이징 적용해야됨
    // 상품id, size 매개변수로 받아서 처리하기
    public List<ItemMartNewResponseDto> getMartNewItem(MartShop martShop, User user) {
        List<Item> itemList = itemRepository.findByMartShopOrderByCreatedAtDesc(martShop);

        List<ItemMartNewResponseDto> itemMartNewResponseDtoList = itemList.stream()
                .map(item -> {
                    ItemMartNewResponseDto itemMartNewResponseDto = ItemMartNewResponseDto.builder()
                            .itemId(item.getItemId())
                            .itemImg(item.getProfilePhoto())
                            .itemName(item.getItemName())
                            .itemPrice(item.getPrice())
                            .isLike(itemLikeService.checkItemLike(item, user))
                            // 마트 정보
                            .mart(ItemMartNewResponseDto.Mart.builder()
                                    .martId(martShop.getMartShopId())
                                    .martName(martShop.getName())
                                    .build())
                            .build();
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
    public void addItemWithImage(MultipartFile profileImage, MultipartFile contentImage, int itemId) {

        Item item = itemRepository.findByItemId(itemId).get();

        ImageDto.ImageRequest imageRequest = new ImageDto.ImageRequest("item", (long) item.getItemId());
        String profileUrl = imageService.createImage(profileImage, imageRequest);
        String contentUrl = imageService.createImage(contentImage, imageRequest);

        item.setPic(profileUrl, contentUrl);

        itemRepository.save(item);
    }
}
