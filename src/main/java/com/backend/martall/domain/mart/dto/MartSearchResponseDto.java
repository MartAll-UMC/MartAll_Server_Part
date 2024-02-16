package com.backend.martall.domain.mart.dto;

import com.backend.martall.domain.item.dto.ItemMartNewResponseDto;
import com.backend.martall.domain.mart.entity.MartShop;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MartSearchResponseDto {
    private Long martId;
    private String name;
    private String location;
    private List<String> categories;
    private int followersCount;
    private int likeCount;
    private List<ItemMartNewResponseDto> items;
    private boolean bookmarkYn;

    public static MartSearchResponseDto of(MartShop martShop) {
        return MartSearchResponseDto.builder()
                .martId(martShop.getMartShopId())
                .name(martShop.getName())
                .location(martShop.getAddress())
                .followersCount(martShop.getMartBookmarks().size())
                .build();
    }
}