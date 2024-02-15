package com.backend.martall.domain.mart.dto;

import com.backend.martall.domain.mart.entity.MartShop;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MartFilterResponseDto {
    private Long martId;
    private String name;
    private String location;
    private List<String> categories;
    private int followersCount;
    private int likeCount;
//    private List<> items;  -->  나중에 추가하기
    private boolean bookmarkYn;

    public static MartFilterResponseDto of(MartShop martShop) {
        return MartFilterResponseDto.builder()
                .martId(martShop.getMartShopId())
                .name(martShop.getName())
                .location(martShop.getAddress())
                .followersCount(martShop.getMartBookmarks().size())
                .build();
    }
}
