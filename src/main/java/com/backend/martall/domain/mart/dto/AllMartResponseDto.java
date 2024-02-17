package com.backend.martall.domain.mart.dto;

import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.mart.repository.MartBookmarkRepository;
import com.backend.martall.domain.user.entity.User;
import com.backend.martall.domain.user.entity.UserRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AllMartResponseDto {
    private Long martId;
    private String name;
    private String location;
    private List<String> categories;
    private int followersCount;
    private int visitorsCount;
    private String imageUrl;
    private List<ItemDto> items;
    private int totalLikes;
    private boolean isFollowed;


    @Getter
    @Setter
    @NoArgsConstructor
    public static class ItemDto {
        private int itemId;
        private String name;
        private String imageUrl;
        private int price;
        private String favoriteLink;
        private String detailLink;
    }

}
