package com.backend.martall.domain.mart.service;

import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.item.repository.ItemRepository;
import com.backend.martall.domain.itemlike.repository.ItemLikeRepository;
import com.backend.martall.domain.mart.dto.*;
import com.backend.martall.domain.mart.entity.MartCategory;
import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.mart.entity.MartTag;
import com.backend.martall.domain.mart.repository.MartBookmarkRepository;
import com.backend.martall.domain.mart.repository.MartRepository;
import com.backend.martall.domain.user.entity.User;
import com.backend.martall.domain.user.entity.UserRepository;
import com.backend.martall.global.exception.BadRequestException;
import com.backend.martall.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.backend.martall.global.exception.ResponseStatus.MART_NAME_NOT_FOUND;
import static com.backend.martall.global.exception.ResponseStatus.NOT_EXIST_USER;

@Service
@RequiredArgsConstructor
public class MartService {

    private final MartRepository martRepository;
    private final UserRepository userRepository;
    private final MartBookmarkRepository martBookmarkRepository;
    private final ItemLikeRepository itemLikeRepository;
    private final ItemRepository itemRepository;


    // 마트샵 생성 (테스트)
    @Transactional
    public void createMartTest(MartRequestDto requestDto) {
        try {
            MartShop martShop = requestDto.toEntity();
            martRepository.save(martShop);
        } catch (Exception e) {
            throw new BadRequestException(ResponseStatus.MART_CREATE_FAIL);
        }
    }


    // 마트 생성
    public void createMart(Long userIdx, MartCreateRequestDto martCreateRequestDto) {
        User user = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new BadRequestException(ResponseStatus.NOT_EXIST_USER));

        MartShop martShop = martCreateRequestDto.toEntity();
        martShop.addUser(user);

        martRepository.save(martShop);
    }

    //마트 정보 업데이트
    @Transactional
    public MartUpdateResponseDto updateMart(Long shopId, MartRequestDto requestDto, Long userId) {
        MartShop martShop = martRepository.findById(shopId)
                .orElseThrow(() -> new BadRequestException(MART_NAME_NOT_FOUND));

        // 마트 정보 업데이트 로직
        martShop.updateMartShop(requestDto);

        // 저장 및 DTO 변환
        MartShop updatedMartShop = martRepository.save(martShop);
        return MartUpdateResponseDto.from(updatedMartShop, userId, martBookmarkRepository, userRepository);
    }

    // shopId로 마트샵의 상세정보 조회
    public MartDetailResponseDto getMartDetail(Long shopId, Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new BadRequestException(ResponseStatus.NOT_EXIST_USER));

        MartShop martShop = martRepository.findById(shopId)
                .orElseThrow(() -> new BadRequestException(ResponseStatus.MART_DETAIL_FAIL));

        // dto 생성
        return MartDetailResponseDto.builder()
                .martId(martShop.getMartShopId())
                .martImg(martShop.getProfilePhoto())
                .martName(martShop.getName())
                .martCategory(martShop.getMartCategories().stream()
                        .map(MartCategory::getCategoryName)
                        .collect(Collectors.toList()))
                .bookmarkCount(martShop.getMartBookmarks().size())
                .likeCount(itemLikeRepository.countItemLikeByMart(martShop))
                .martBookmark(martBookmarkRepository.existsByUserAndMartShop(user, martShop))
                .martAddress(martShop.getAddress())
                // --> 나중에 만든 회원명으로 교체
                .martOwner(martShop.getManagerName())
                .martNumber(martShop.getShopNumber())
                .martOperationTime(martShop.getOperatingTime())
                .build();
    }


    //키워드로 마트 검색
    // -> 페이징 추가 필요
    public List<MartResponseDto> searchMarts(String keyword, Long userIdx) {

        User user = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new BadRequestException(ResponseStatus.NOT_EXIST_USER));
        List<MartShop> martShopList = martRepository.findByKeyword(keyword);

        return generateMartList(martShopList, user);
    }


    // 카테고리 지수 검색
    public List<MartWithItemResponseDto> searchMartsByCategoryAndRating(String tag, Integer minBookmark, Integer maxBookmark, Integer minLike, Integer maxLike, String sort, Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new BadRequestException(ResponseStatus.NOT_EXIST_USER));

        // 존재하지 않는 태그면 예외처리
        if (!MartTag.existByName(tag)) {
            throw new BadRequestException(ResponseStatus.MART_TAG_WRONG);
        }

        List<MartShop> martShopList = switch (sort) {
            case "기본" -> martRepository.searchByFilter(tag, minBookmark, maxBookmark, minLike, maxLike);
            case "최신" -> martRepository.searchByFilterCreatedAtDesc(tag, minBookmark, maxBookmark, minLike, maxLike);
            case "단골" -> martRepository.searchByFilterBookmarkDesc(tag, minBookmark, maxBookmark, minLike, maxLike);
            case "찜" -> martRepository.searchByFilterLikeDesc(tag, minBookmark, maxBookmark, minLike, maxLike);
            default -> throw new BadRequestException(ResponseStatus.MART_SORT_WRONG);
        };

        return generateMartWithItemList(martShopList, user);
    }


    //mart 전체 조회
    public List<MartWithItemResponseDto> findAllMarts(Long userIdx) {
        User user = userRepository.findById(userIdx).orElseThrow(() -> new BadRequestException(NOT_EXIST_USER));
        List<MartShop> martShopList = martRepository.findAll();

        return generateMartWithItemList(martShopList, user);
    }

    // 상품 없는 마트 정보 반환
    // (키워드 검색, 단골마트 조회)
    public List<MartResponseDto> generateMartList(List<MartShop> martShopList, User user) {
        return martShopList.stream()
                // 각 martshop 객체 dto로 변경
                .map(martShop -> MartResponseDto.builder()
                        .martId(martShop.getMartShopId())
                        .martName(martShop.getName())
                        .martCategory(martShop.getMartCategories().stream()
                                .map(MartCategory::getCategoryName)
                                .collect(Collectors.toList()))
                        .bookmarkCount(martShop.getMartBookmarks().size())
                        .likeCount(itemLikeRepository.countItemLikeByMart(martShop))
                        .martBookmark(martBookmarkRepository.existsByUserAndMartShop(user, martShop))
                        .build())
                .toList();
    }

    // 상품과 함께 마트 정보 반환
    // (마트 전체 검색, 마트 필터 검색)
    public List<MartWithItemResponseDto> generateMartWithItemList(List<MartShop> martShopList, User user) {
        return martShopList.stream()
                .map(martShop -> MartWithItemResponseDto.builder()
                        .martId(martShop.getMartShopId())
                        .martName(martShop.getName())
                        .martCategory(martShop.getMartCategories().stream()
                                .map(MartCategory::getCategoryName)
                                .collect(Collectors.toList()))
                        .bookmarkCount(martShop.getMartBookmarks().size())
                        .likeCount(itemLikeRepository.countItemLikeByMart(martShop))
                        .martBookmark(martBookmarkRepository.existsByUserAndMartShop(user, martShop))
                        .items(getMartItemCreatedAtDesc(martShop, user))
                        .build())
                .toList();
    }

    // 마트의 상품 불러오기 (새상품순)
    public List<MartWithItemResponseDto.ItemDto> getMartItemCreatedAtDesc(MartShop martShop, User user) {

        List<Item> itemList = itemRepository.findByMartShopOrderByCreatedAtDesc(martShop);

        return itemList.stream()
                .map(item -> MartWithItemResponseDto.ItemDto.builder()
                        .itemId(item.getItemId())
                        .itemImg(item.getProfilePhoto())
                        .itemName(item.getItemName())
                        .itemPrice(item.getPrice())
                        .itemLike(itemLikeRepository.existsByUserAndItem(user, item))
                        .build())
                .toList();
    }

    // 마트 상품 불러오기
    public List<MartItemResponseDto> getMartItem(Long martId, Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx)
                .orElseThrow(() -> new BadRequestException(ResponseStatus.NOT_EXIST_USER));

        MartShop martShop = martRepository.findById(martId)
                .orElseThrow(() -> new BadRequestException(MART_NAME_NOT_FOUND));

        List<Item> itemList = itemRepository.findByMartShopOrderByCreatedAtDesc(martShop);

        return itemList.stream()
                .map(item -> MartItemResponseDto.builder()
                            .itemId(item.getItemId())
                            .itemImg(item.getProfilePhoto())
                            .itemName(item.getItemName())
                            .itemPrice(item.getPrice())
                            .itemLike(itemLikeRepository.existsByUserAndItem(user, item))
                            // 마트 정보
                            .mart(MartItemResponseDto.Mart.builder()
                                    .martId(martShop.getMartShopId())
                                    .martName(martShop.getName())
                                    .build())
                            .build())
                .toList();
    }


    // 오늘의 마트 조회
    // 랜덤 5개 마트
    public List<MartRecommendedResponseDto> getTodayMart() {
        List<MartShop> martShopList = martRepository.findRandomMart(PageRequest.of(0, 5));
        return martShopList.stream()
                .map(martShop -> {
                    return MartRecommendedResponseDto.builder()
                            .martId(martShop.getMartShopId())
                            .martImg(martShop.getProfilePhoto())
                            .martName(martShop.getName())
                            .category(martShop.getMartCategories().stream()
                                    .map(MartCategory::getCategoryName)
                                    .collect(Collectors.toList()))
                            .build();
                })
                .collect(Collectors.toList());
    }

    // 마트명으로 10개의 키워드 추천
    public List<String> recommendMartKeyword() {
        List<MartShop> martShopList = martRepository.findRandomMart(PageRequest.of(0, 10));

        return martShopList.stream()
                .map(MartShop::getName)
                .toList();
    }

    // 로그인한 유저가 마트를 소유하고 있는지 확인
    public MartExistResponseDto checkOwnMart(Long userIdx) {
        User user = userRepository.findById(userIdx)
                .orElseThrow(() -> new BadRequestException(ResponseStatus.NOT_EXIST_USER));

        return MartExistResponseDto.builder()
                .martExist(martRepository.existsByUser(user))
                .build();
    }
}



