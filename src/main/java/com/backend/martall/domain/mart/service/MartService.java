package com.backend.martall.domain.mart.service;

import com.backend.martall.domain.item.service.ItemService;
import com.backend.martall.domain.itemlike.service.ItemLikeService;
import com.backend.martall.domain.mart.dto.*;
import com.backend.martall.domain.mart.entity.MartTag;
import com.backend.martall.domain.user.entity.User;
import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.mart.repository.MartRepository;
import com.backend.martall.domain.user.entity.UserRepository;
import com.backend.martall.domain.mart.repository.MartCategoryRepository;
import com.backend.martall.domain.mart.repository.MartBookmarkRepository;
import com.backend.martall.global.exception.BadRequestException;
import com.backend.martall.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.backend.martall.domain.mart.entity.MartCategory;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MartService {

    private final MartRepository martRepository;
    private final UserRepository userRepository;
    private final MartBookmarkRepository martBookmarkRepository;
    private final ItemLikeService itemLikeService;

    private final ItemService itemService;


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
        User user = userRepository.findByUserIdx(userIdx).get();

        MartShop martShop = martCreateRequestDto.toEntity();
        martShop.addUser(user);

        martRepository.save(martShop);
    }

    //마트 정보 업데이트
    @Transactional
    public MartUpdateResponseDto updateMart(Long shopId, MartRequestDto requestDto, Long userId) {
        MartShop martShop = martRepository.findById(shopId)
                .orElseThrow(() -> new BadRequestException(ResponseStatus.MART_NAME_NOT_FOUND));

        // 마트 정보 업데이트 로직
        martShop.updateMartShop(requestDto);

        // 저장 및 DTO 변환
        MartShop updatedMartShop = martRepository.save(martShop);
        return MartUpdateResponseDto.from(updatedMartShop, userId, martBookmarkRepository, userRepository);
    }

    // shopId로 마트샵의 상세정보 조회
    public MartDetailResponseDto getMartDetail(Long shopId, Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx).get();

        MartShop martShop = martRepository.findById(shopId)
                .orElseThrow(() -> new BadRequestException(ResponseStatus.MART_DETAIL_FAIL));

        // dto 생성
        MartDetailResponseDto martDetailResponseDto = MartDetailResponseDto.builder()
                .martId(martShop.getMartShopId())
                .martImg(martShop.getProfilePhoto())
                .martName(martShop.getName())
                .martCategory(martShop.getMartCategories().stream()
                        .map(MartCategory::getCategoryName)
                        .collect(Collectors.toList()))
                .bookmarkCount(martShop.getMartBookmarks().size())
                .likeCount(itemLikeService.countItemLikeByMart(martShop))
                .martBookmark(martBookmarkRepository.existsByUserAndMartShop(user, martShop))
                .martAddress(martShop.getAddress())
                // --> 나중에 만든 회원명으로 교체
                .martOwner(martShop.getManagerName())
                .martNumber(martShop.getShopNumber())
                .martOperationTime(martShop.getOperatingTime())
                .build();

        return martDetailResponseDto;
    }


    //키워드로 마트 검색
    // -> 페이징 추가 필요
    public List<MartResponseDto> searchMarts(String keyword, Long userIdx) {

        User user = userRepository.findByUserIdx(userIdx).get();
        List<MartShop> martShopList = martRepository.findByKeyword(keyword);

        return generateMartList(martShopList, user);
    }


    // 카테고리 지수 검색
    public List<MartWithItemResponseDto> searchMartsByCategoryAndRating(String tag, Integer minBookmark, Integer maxBookmark, Integer minLike, Integer maxLike, String sort, Long userIdx) {
        User user = userRepository.findByUserIdx(userIdx).get();

        // 존재하지 않는 태그면 예외처리
        if (!MartTag.existByName(tag)) {
            throw new BadRequestException(ResponseStatus.MART_TAG_WRONG);
        }

        List<MartShop> martShopList;
        if (sort.equals("기본")) {
            martShopList = martRepository.searchByFilter(tag, minBookmark, maxBookmark, minLike, maxLike);
        } else if (sort.equals("최신")) {
            martShopList = martRepository.searchByFilterCreatedAtDesc(tag, minBookmark, maxBookmark, minLike, maxLike);
        } else if (sort.equals("단골")) {
            martShopList = martRepository.searchByFilterBookmarkDesc(tag, minBookmark, maxBookmark, minLike, maxLike);
        } else if (sort.equals("찜")) {
            martShopList = martRepository.searchByFilterLikeDesc(tag, minBookmark, maxBookmark, minLike, maxLike);
        } else {
            throw new BadRequestException(ResponseStatus.MART_SORT_WRONG);
        }

        return generateMartWithItemList(martShopList, user);
    }


    //mart 전체 조회
    public List<MartWithItemResponseDto> findAllMarts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException(ResponseStatus.NOT_EXIST_USER));
        List<MartShop> martShopList = martRepository.findAll();

        return generateMartWithItemList(martShopList, user);
    }

    // 상품 없는 마트 정보 반환
    // (키워드 검색, 단골마트 조회)
    public List<MartResponseDto> generateMartList(List<MartShop> martShopList, User user) {
        List<MartResponseDto> martResponseDtoList = martShopList.stream()
                // 각 martshop 객체 dto로 변경
                .map(martShop -> {
                    MartResponseDto martKeywordSearchResponseDto = MartResponseDto.builder()
                            .martId(martShop.getMartShopId())
                            .martName(martShop.getName())
                            .martCategory(martShop.getMartCategories().stream()
                                    .map(MartCategory::getCategoryName)
                                    .collect(Collectors.toList()))
                            .bookmarkCount(martShop.getMartBookmarks().size())
                            .likeCount(itemLikeService.countItemLikeByMart(martShop))
                            .martBookmark(martBookmarkRepository.existsByUserAndMartShop(user, martShop))
                            .build();

                    return martKeywordSearchResponseDto;
                })
                .collect(Collectors.toList());

        return martResponseDtoList;
    }

    // 상품과 함께 마트 정보 반환
    // (마트 전체 검색, 마트 필터 검색)
    public List<MartWithItemResponseDto> generateMartWithItemList(List<MartShop> martShopList, User user) {
        List<MartWithItemResponseDto> martWithItemResponseDtoList = martShopList.stream()
                .map(martShop -> {
                    MartWithItemResponseDto martWithItemResponseDto = MartWithItemResponseDto.builder()
                            .martId(martShop.getMartShopId())
                            .martName(martShop.getName())
                            .martCategory(martShop.getMartCategories().stream()
                                    .map(martCategory -> martCategory.getCategoryName())
                                    .collect(Collectors.toList()))
                            .bookmarkCount(martShop.getMartBookmarks().size())
                            .likeCount(itemLikeService.countItemLikeByMart(martShop))
                            .martBookmark(martBookmarkRepository.existsByUserAndMartShop(user, martShop))
                            .items(itemService.getMartNewItem(martShop, user))
                            .build();

                    return martWithItemResponseDto;

                })
                .collect(Collectors.toList());

        return martWithItemResponseDtoList;
    }

}



