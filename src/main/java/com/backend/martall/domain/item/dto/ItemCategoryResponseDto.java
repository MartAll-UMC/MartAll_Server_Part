package com.backend.martall.domain.item.dto;

import com.backend.martall.domain.item.entity.Item;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemCategoryResponseDto {
    private int itemId;
    private String pic;
    private String itemName;
    private String martShopName;
    private int price;
    private boolean like;

    public static ItemCategoryResponseDto from(Item item) {
        return ItemCategoryResponseDto.builder()
                .itemId(item.getItemId())
                .pic(item.getProfilePhoto())
                .itemName(item.getItemName())
                .martShopName(item.getMartShop().getName())
                .price(item.getPrice())
                .build();
    }
}
