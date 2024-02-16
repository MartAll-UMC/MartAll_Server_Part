package com.backend.martall.domain.mart.service;

import com.backend.martall.domain.user.entity.User;
import com.backend.martall.domain.mart.entity.MartBookmark;
import com.backend.martall.domain.mart.dto.MartRequestDto;
import com.backend.martall.domain.mart.dto.MartResponseDto;
import com.backend.martall.domain.mart.dto.AllMartResponseDto;
import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.mart.dto.FollowedMartResponseDto;
import com.backend.martall.domain.mart.repository.MartRepository;
import com.backend.martall.domain.user.entity.UserRepository;
import com.backend.martall.domain.mart.repository.MartCategoryRepository;
import com.backend.martall.domain.mart.repository.MartBookmarkRepository;
import com.backend.martall.global.exception.BadRequestException;
import com.backend.martall.global.exception.ResponseStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.backend.martall.domain.mart.entity.MartCategory;

import java.util.List;
import java.util.stream.Collectors;
import com.backend.martall.domain.itemlike.service.ItemLikeService;

@Service
public class MartService {

    private final MartRepository martRepository;
    private final UserRepository userRepository;
    private final MartCategoryRepository martCategoryRepository;
    private final MartBookmarkRepository martBookmarkRepository;
    private final ItemLikeService itemLikeService;


    public MartService(MartRepository martRepository, UserRepository userRepository, MartCategoryRepository martCategoryRepository, MartBookmarkRepository martBookmarkRepository, ItemLikeService itemLikeService) {
        this.martRepository = martRepository;
        this.userRepository = userRepository;
        this.martCategoryRepository = martCategoryRepository;
        this.martBookmarkRepository = martBookmarkRepository;
        this.itemLikeService = itemLikeService;
    }

    // 마트샵 생성
    @Transactional
    public MartResponseDto createMart(MartRequestDto requestDto, Long userId) {
        try {
            MartShop martShop = requestDto.toEntity();
            MartShop savedMartShop = martRepository.save(martShop);
            return MartResponseDto.from(savedMartShop, userId, martBookmarkRepository, userRepository);
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
    public MartResponseDto getMartDetail(Long shopId) {
        MartShop martShop = martRepository.findById(shopId)
                .orElseThrow(() -> new BadRequestException(ResponseStatus.MART_DETAIL_FAIL));
        return MartResponseDto.from(martShop, null, martBookmarkRepository, userRepository);
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
    public void unfollowMart(Long shopId, Long userId) {
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
            dto.setMartshopId(martShop.getMartShopId());
            dto.setMartname(martShop.getName());
            dto.setVisitorCount(martShop.getVisitor());
           // dto.setSalesIndex(calculateSalesIndex(martShop)); 판매지수 -
            dto.setMartcategory(martShop.getMartCategories().stream()
                    .map(MartCategory::getCategoryName)
                    .collect(Collectors.toList()));
            dto.setIsfollowed(true);

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

    //mart 전체 조회
    public List<AllMartResponseDto> findAllMarts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException(ResponseStatus.NOT_EXIST_USER));
        List<MartShop> martShops = martRepository.findAll();
        return martShops.stream().map(martShop -> {
            AllMartResponseDto dto = convertToAllMartResponseDto(martShop);
            int totalLikes = itemLikeService.countItemLikeByMart(martShop); // 각 마트별 상품의 총 찜 수 계산
            dto.setTotalLikes(totalLikes);
            boolean isFollowed = martBookmarkRepository.existsByUserAndMartShop(user, martShop); // 단골 여부 확인
           // dto.setIsFollowed(isFollowed); -setIsFollowed왜 인식못함
            return dto;
        }).collect(Collectors.toList());
    }

    private AllMartResponseDto convertToAllMartResponseDto(MartShop martShop) {
        AllMartResponseDto dto = new AllMartResponseDto();
        dto.setMartId(martShop.getMartShopId());
        dto.setName(martShop.getName());
        dto.setLocation(martShop.getAddress());
        return dto;
    }
    }
//    public List<MartResponseDto> searchMartsByCategoryAndRating(String tag, Integer minBookmark, Integer maxBookmark, Integer minLike, Integer maxLike, String sort, Long userIdx) {
//
//
//        List<MartShop> martShops = martRepository.findByCategoryName(category);
//        return martShops.stream()
//                .map(martShop -> MartResponseDto.from(martShop, null, martBookmarkRepository, userRepository))
//                .collect(Collectors.toList());
//    }



