package com.backend.martall.domain.mart.service;

import com.backend.martall.domain.item.service.ItemService;
import com.backend.martall.domain.itemlike.service.ItemLikeService;
import com.backend.martall.domain.mart.dto.*;
import com.backend.martall.domain.mart.entity.MartTag;
import com.backend.martall.domain.user.entity.User;
import com.backend.martall.domain.mart.entity.MartBookmark;
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
    private final MartCategoryRepository martCategoryRepository;
    private final MartBookmarkRepository martBookmarkRepository;
    private final ItemLikeService itemLikeService;

    private final ItemService itemService;


    // 마트샵 생성
    @Transactional
    public void createMart(MartRequestDto requestDto) {
        try {
            MartShop martShop = requestDto.toEntity();
            martRepository.save(martShop);
        } catch (Exception e) {
            throw new BadRequestException(ResponseStatus.MART_CREATE_FAIL);
        }
    }

    //마트 정보 업데이트
    @Transactional
    public MartResponseDto updateMart(Long shopId, MartRequestDto requestDto, Long userId) {
        MartShop martShop = martRepository.findById(shopId)
                .orElseThrow(() -> new BadRequestException(ResponseStatus.MART_NAME_NOT_FOUND));

        // 마트 정보 업데이트 로직
        if (requestDto.getName() != null) martShop.setName(requestDto.getName());
        if (requestDto.getIntroduction() != null) martShop.setIntroduction(requestDto.getIntroduction());
        if (requestDto.getAddress() != null) martShop.setAddress(requestDto.getAddress());
        if (requestDto.getOperatingTime() != null) martShop.setOperatingTime(requestDto.getOperatingTime());
        if (requestDto.getPickupTime() != null) martShop.setPickupTime(requestDto.getPickupTime());
        if (requestDto.getVisitor() != null) martShop.setVisitor(requestDto.getVisitor());
        if (requestDto.getSale() != null) martShop.setSale(requestDto.getSale());
        if (requestDto.getProfilePhoto() != null) martShop.setProfilePhoto(requestDto.getProfilePhoto());
        if (requestDto.getManagerName() != null) martShop.setManagerName(requestDto.getManagerName());
        if (requestDto.getShopNumber() != null) martShop.setShopNumber(requestDto.getShopNumber());
        if (requestDto.getLinkKakao() != null) martShop.setLinkKakao(requestDto.getLinkKakao());
        if (requestDto.getLinkNaver() != null) martShop.setLinkNaver(requestDto.getLinkNaver());
        if (requestDto.getLongitude() != null) martShop.setLongitude(requestDto.getLongitude());
        if (requestDto.getLatitude() != null) martShop.setLatitude(requestDto.getLatitude());

        // 저장 및 DTO 변환
        MartShop updatedMartShop = martRepository.save(martShop);
        return MartResponseDto.from(updatedMartShop, userId, martBookmarkRepository, userRepository);
    }

    // shopId로 마트샵의 상세정보 조회
    public MartDetailResponseDto getMartDetail(Long shopId) {
        MartShop martShop = martRepository.findById(shopId)
                .orElseThrow(() -> new BadRequestException(ResponseStatus.MART_DETAIL_FAIL));
        MartDetailResponseDto martDetailResponseDto = MartDetailResponseDto.builder()
                .martshopId(martShop.getMartShopId())
                .name(martShop.getName())
                .ownerName(martShop.getManagerName())
                .shopnumber(martShop.getShopNumber())
                .email("martall@gmail.com")
                .phonenumber("010-xxxx-xxxx")
                .operatingHours(martShop.getOperatingTime())
                .pickuptime(martShop.getPickupTime())
                .payment("카드결제")
                .address(martShop.getAddress())
                .kakaoTalkLink(martShop.getLinkKakao())
                .build();
        return martDetailResponseDto;
    }

    // 단골 마트 추가
    @Transactional
    public void followMart(Long userId, Long shopId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException(ResponseStatus.NOT_EXIST_USER));
        MartShop martShop = martRepository.findById(shopId).orElseThrow(() -> new BadRequestException(ResponseStatus.MART_NAME_NOT_FOUND));
        boolean isAlreadyFavorite = martBookmarkRepository.existsByUserAndMartShop(user, martShop);
        if (isAlreadyFavorite) {
            throw new BadRequestException(ResponseStatus.MART_FAVORITE_ALREADY);
        }
        MartBookmark martBookmark = new MartBookmark();
        martBookmark.setUser(user);
        martBookmark.setMartShop(martShop);
        martBookmarkRepository.save(martBookmark);
    }

    // 단골 마트에서 삭제
    @Transactional
    public void unfollowMart(Long userId, Long shopId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException(ResponseStatus.NOT_EXIST_USER));
        MartShop martShop = martRepository.findById(shopId).orElseThrow(() -> new BadRequestException(ResponseStatus.MART_NAME_NOT_FOUND));
        MartBookmark martBookmark = martBookmarkRepository.findByUserAndMartShop(user, martShop).orElseThrow(() -> new BadRequestException(ResponseStatus.MART_FAVORITE_NOT_FOUND));
        martBookmarkRepository.delete(martBookmark);
    }

    // 단골 마트 목록 조회
    public List<FollowedMartResponseDto> getFollowedMarts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException(ResponseStatus.NOT_EXIST_USER));
        List<MartBookmark> bookmarks = martBookmarkRepository.findByUser(user);

        return bookmarks.stream().map(bookmark -> {
            MartShop martShop = bookmark.getMartShop();
            FollowedMartResponseDto dto = new FollowedMartResponseDto();

            dto.setBookmarkId(bookmark.getBookmarkId());
            dto.setMartShopId(martShop.getMartShopId());
            dto.setMartName(martShop.getName());
            dto.setLikeCount(itemLikeService.countItemLikeByMart(martShop));
            dto.setBookmarkCount(martShop.getMartBookmarks().size());
            dto.setMartCategory(martShop.getMartCategories().stream()
                    .map(MartCategory::getCategoryName)
                    .collect(Collectors.toList()));

            return dto;
        }).collect(Collectors.toList());
    }

    //search mart by keyword
    public List<MartResponseDto> searchMarts(String keyword, Long userId) {
        List<MartShop> martShops = martRepository.findByKeyword(keyword);
        return martShops.stream()
                .map(martShop -> MartResponseDto.from(martShop, userId, martBookmarkRepository, userRepository))
                .collect(Collectors.toList());
    }


    // 카테고리 지수 검색
    public List<MartSearchResponseDto> searchMartsByCategoryAndRating(String tag, Integer minBookmark, Integer maxBookmark, Integer minLike, Integer maxLike, String sort, Long userIdx) {
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

        List<MartSearchResponseDto> martFilterResponseDtoList = martShopList.stream()
                .map(martShop -> {
                    MartSearchResponseDto martSearchResponseDto = MartSearchResponseDto.of(martShop);

                    // 카테고리 채우기
                    List<String> categoryList = martShop.getMartCategories().stream()
                            .map(martCategory -> martCategory.getCategoryName())
                            .collect(Collectors.toList());
                    martSearchResponseDto.setCategories(categoryList);

                    martSearchResponseDto.setLikeCount(itemLikeService.countItemLikeByMart(martShop));

                    martSearchResponseDto.setBookmarkYn(martBookmarkRepository.existsByUserAndMartShop(user, martShop));

                    // 아이템 리스트 채우기
                    martSearchResponseDto.setItems(itemService.getMartNewItem(martShop, user));
                    return martSearchResponseDto;

                })
                .collect(Collectors.toList());

        return martFilterResponseDtoList;
    }


    //mart 전체 조회
    public List<MartSearchResponseDto> findAllMarts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException(ResponseStatus.NOT_EXIST_USER));
        List<MartShop> martShopList = martRepository.findAll();
        List<MartSearchResponseDto> martFilterResponseDtoList = martShopList.stream()
                .map(martShop -> {
                    MartSearchResponseDto martSearchResponseDto = MartSearchResponseDto.of(martShop);

                    // 카테고리 채우기
                    List<String> categoryList = martShop.getMartCategories().stream()
                            .map(martCategory -> martCategory.getCategoryName())
                            .collect(Collectors.toList());
                    martSearchResponseDto.setCategories(categoryList);

                    martSearchResponseDto.setLikeCount(itemLikeService.countItemLikeByMart(martShop));

                    martSearchResponseDto.setBookmarkYn(martBookmarkRepository.existsByUserAndMartShop(user, martShop));

                    // 아이템 리스트 채우기
                    martSearchResponseDto.setItems(itemService.getMartNewItem(martShop, user));
                    return martSearchResponseDto;

                })
                .collect(Collectors.toList());

        return martFilterResponseDtoList;
    }

}



