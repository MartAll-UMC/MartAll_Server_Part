package com.backend.martall.domain.item.dto;

import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.user.entity.User;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemListResponseDto {

    private int itemId;
    private String pic;
    private String categoryName;
    private String martShopName;
    private String itemName;
    private int price;
    private String content;
    private boolean like;

    public static ItemListResponseDto from(Item item) {
        return ItemListResponseDto.builder()
                .itemId(item.getItemId())
                .pic(item.getProfilePhoto())
                .categoryName(item.getCategoryId().getName())
                .martShopName(item.getMartShop().getName())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .content(item.getContent())
                .build();
    }
}
