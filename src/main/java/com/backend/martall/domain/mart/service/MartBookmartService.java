package com.backend.martall.domain.mart.service;

import com.backend.martall.domain.itemlike.service.ItemLikeService;
import com.backend.martall.domain.mart.dto.FollowedMartResponseDto;
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
public class MartBookmartService {
    private UserRepository userRepository;
    private MartRepository martRepository;
    private MartBookmarkRepository martBookmarkRepository;
    private ItemLikeService itemLikeService;

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
}
