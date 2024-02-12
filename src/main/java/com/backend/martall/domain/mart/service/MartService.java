package com.backend.martall.domain.mart.service;
import com.backend.martall.domain.user.entity.User;
import com.backend.martall.domain.mart.entity.MartCategory;
import com.backend.martall.domain.mart.entity.MartBookmark;

import com.backend.martall.domain.mart.dto.MartRequestDto;
import com.backend.martall.domain.mart.dto.MartResponseDto;
import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.mart.repository.MartRepository;
import com.backend.martall.domain.user.entity.UserRepository;
import com.backend.martall.domain.mart.repository.MartCategoryRepository;
import com.backend.martall.domain.mart.repository.MartBookmarkRepository;

import com.backend.martall.global.exception.BadRequestException;
import com.backend.martall.global.exception.ResponseStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MartService {

    private final MartRepository martRepository;
    private final UserRepository userRepository;
    private final MartCategoryRepository martCategoryRepository;

    private final MartBookmarkRepository martBookmarkRepository;
    public MartService(MartRepository martRepository, UserRepository userRepository, MartCategoryRepository martCategoryRepository, MartBookmarkRepository martBookmarkRepository) {
        this.martRepository = martRepository;
        this.userRepository = userRepository;
        this.martCategoryRepository = martCategoryRepository;
        this.martBookmarkRepository = martBookmarkRepository;
    }

    // 마트샵 생성
    @Transactional
    public MartResponseDto createMart(MartRequestDto requestDto) {
        try {
            MartShop martShop = requestDto.toEntity();
            MartShop savedMartShop = martRepository.save(martShop);
            return MartResponseDto.from(savedMartShop);
        } catch (Exception e) {
            // 마트 생성 중 오류 발생 시
            throw new BadRequestException(ResponseStatus.MART_CREATE_FAIL);
        }
    }

    // 마트 정보 업데이트
    @Transactional
    public MartResponseDto updateMart(Long shopId, MartRequestDto requestDto) {
        MartShop martShop = martRepository.findById(shopId)
                .orElseThrow(() -> new BadRequestException(ResponseStatus.MART_NAME_NOT_FOUND));

        // User 엔티티를 찾아서 설정
        User user = userRepository.findById(requestDto.getUserIdx())
                .orElseThrow(() -> new BadRequestException(ResponseStatus.MART_CREATE_FAIL));
        martShop.setUser(user);

        MartCategory martCategory = martCategoryRepository.findById(requestDto.getMartCategoryId())
                .orElseThrow(() -> new BadRequestException(ResponseStatus.MART_CATEGORY_NOT_FOUND));
        martShop.addMartCategory(martCategory);

        martShop.setName(requestDto.getName());
        martShop.setIntroduction(requestDto.getIntroduction());
        martShop.setAddress(requestDto.getAddress());
        martShop.setOperatingTime(requestDto.getOperatingTime());
        martShop.setPickupTime(requestDto.getPickupTime());
        martShop.setVisitor(requestDto.getVisitor());
        martShop.setSale(requestDto.getSale());
        martShop.setProfilePhoto(requestDto.getProfilePhoto());
        martShop.setManagerName(requestDto.getManagerName());
        martShop.setShopNumber(requestDto.getShopNumber());
        martShop.setLinkKakao(requestDto.getLinkKakao());
        martShop.setLinkNaver(requestDto.getLinkNaver());
        martShop.setLongitude(requestDto.getLongitude());
        martShop.setLatitude(requestDto.getLatitude());

        MartShop updatedMartShop = martRepository.save(martShop);
        return MartResponseDto.from(updatedMartShop);
    }


    // 마트샵 검색 결과 -> List<MartResponseDto>
    public List<MartResponseDto> searchMarts(String keyword) {
        List<MartShop> martShops = martRepository.findByKeyword(keyword);
        if (martShops.isEmpty()) {
            // 검색 결과가 없을 경우
            throw new BadRequestException(ResponseStatus.MART_NAME_NOT_FOUND);
        }
        return martShops.stream()
                .map(MartResponseDto::from)
                .collect(Collectors.toList());
    }

    // shopId로 마트샵의 상세정보 조회
    public MartResponseDto getMartDetail(Long shopId) {
        MartShop martShop = martRepository.findById(shopId)
                .orElseThrow(() -> new BadRequestException(ResponseStatus.MART_DETAIL_FAIL));
        return MartResponseDto.from(martShop);
    }

    // rating, category filter - Martshop
    public List<MartResponseDto> searchMartsByCategoryAndRating(String category, Double rating) {
        List<MartShop> martShops = martRepository.findByCategoryName(category);
        if (martShops.isEmpty()) {
            // 필터 결과가 없을 경우
            throw new BadRequestException(ResponseStatus.MART_FILTER_NOT_FOUND);
        }
        return martShops.stream()
                .map(MartResponseDto::from)
                .collect(Collectors.toList());
    }

    // 단골 마트 추가
    @Transactional
    public void followMart(Long userId, Long shopId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(ResponseStatus.NOT_EXIST_USER));
        MartShop martShop = martRepository.findById(shopId)
                .orElseThrow(() -> new BadRequestException(ResponseStatus.MART_NAME_NOT_FOUND));

        // 이미 단골로 등록되었는지 확인
        boolean isAlreadyFavorite = martBookmarkRepository.existsByUserAndMartShop(user, martShop);
        if (isAlreadyFavorite) {
            throw new BadRequestException(ResponseStatus.MART_FAVORITE_ALREADY);
        }

        MartBookmark martBookmark = new MartBookmark();
        martBookmark.setUser(user);
        martBookmark.setMartShop(martShop);
        martBookmarkRepository.save(martBookmark);
    }
    //단골마트에서 삭제
    @Transactional
    public void unfollowMart(Long shopId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(ResponseStatus.NOT_EXIST_USER));
        MartShop martShop = martRepository.findById(shopId)
                .orElseThrow(() -> new BadRequestException(ResponseStatus.MART_NAME_NOT_FOUND));

        MartBookmark martBookmark = martBookmarkRepository.findByUserAndMartShop(user, martShop)
                .orElseThrow(() -> new BadRequestException(ResponseStatus.MART_FAVORITE_NOT_FOUND));

        martBookmarkRepository.delete(martBookmark);
    }

    //단골마트 목록 조회
    public List<MartResponseDto> getFollowedMarts(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException(ResponseStatus.NOT_EXIST_USER));

        List<MartBookmark> bookmarks = martBookmarkRepository.findByUser(user);

        return bookmarks.stream()
                .map(bookmark -> MartResponseDto.from(bookmark.getMartShop()))
                .collect(Collectors.toList());
    }

}
