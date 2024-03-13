package com.backend.martall.domain.mart.service;

import com.backend.martall.domain.itemlike.service.ItemLikeService;
import com.backend.martall.domain.mart.dto.FollowedMartResponseDto;
import com.backend.martall.domain.mart.dto.MartResponseDto;
import com.backend.martall.domain.mart.entity.MartBookmark;
import com.backend.martall.domain.mart.entity.MartCategory;
import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.mart.repository.MartBookmarkRepository;
import com.backend.martall.domain.mart.repository.MartRepository;
import com.backend.martall.domain.user.entity.User;
import com.backend.martall.domain.user.entity.UserRepository;
import com.backend.martall.global.exception.BadRequestException;
import com.backend.martall.global.exception.ResponseStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MartBookmarkService {
    private UserRepository userRepository;
    private MartRepository martRepository;
    private MartBookmarkRepository martBookmarkRepository;
    private ItemLikeService itemLikeService;

    private MartService martService;

    @Transactional
    public void followMart(Long userId, Long shopId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException(ResponseStatus.NOT_EXIST_USER));
        MartShop martShop = martRepository.findById(shopId).orElseThrow(() -> new BadRequestException(ResponseStatus.MART_NAME_NOT_FOUND));
        boolean isAlreadyFavorite = martBookmarkRepository.existsByUserAndMartShop(user, martShop);
        if (isAlreadyFavorite) {
            throw new BadRequestException(ResponseStatus.MART_FAVORITE_ALREADY);
        }
        MartBookmark martBookmark = new MartBookmark();
        martShop.addBookmark(martBookmark);
        martBookmark.setUser(user);
        martBookmarkRepository.save(martBookmark);
    }

    // 단골 마트에서 삭제
    @Transactional
    public void unfollowMart(Long userId, Long shopId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException(ResponseStatus.NOT_EXIST_USER));
        MartShop martShop = martRepository.findById(shopId).orElseThrow(() -> new BadRequestException(ResponseStatus.MART_NAME_NOT_FOUND));
        MartBookmark martBookmark = martBookmarkRepository.findByUserAndMartShop(user, martShop).orElseThrow(() -> new BadRequestException(ResponseStatus.MART_FAVORITE_NOT_FOUND));
        martShop.removeBookmark(martBookmark);
        martBookmarkRepository.delete(martBookmark);
    }

    // 단골마트 조회
    public List<MartResponseDto> getFollowedMarts(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new BadRequestException(ResponseStatus.NOT_EXIST_USER));

        // 단골마트 조회
        List<MartBookmark> bookmarkList = martBookmarkRepository.findByUser(user);

        // 단골마트 마트 리스트
        List<MartShop> martShopList = bookmarkList.stream()
                .map(martBookmark -> martBookmark.getMartShop())
                .collect(Collectors.toList());

        // dto 리스트 반환
        return martService.generateMartList(martShopList, user);
    }
}
