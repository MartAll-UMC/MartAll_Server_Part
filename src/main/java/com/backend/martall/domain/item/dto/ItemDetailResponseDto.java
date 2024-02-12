package com.backend.martall.domain.item.dto;

import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.item.entity.ItemCategory;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetailResponseDto {

    private int itemId;
    private ItemMartShopResponseDto mart; // 마트 정보가 필요해서 ItemMartShopResponseDto를 추가
    private String categoryName;
    private String itemName;
    private int price;
    private String content;

    public static ItemDetailResponseDto from(Item item) {
        return ItemDetailResponseDto.builder()
                .itemId(item.getItemId())
                .categoryName(item.getCategoryId().getName())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .content(item.getContent())
                .build();
    }
}
