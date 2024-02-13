package com.backend.martall.domain.item.dto;

import com.backend.martall.domain.item.entity.Item;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemNewResponseDto {
    private int itemId;
    private String pic;
    private String categoryName;
    private String itemName;
    private int price;
    private String content;
    private String like;

    public static ItemNewResponseDto from(Item item) {
        return ItemNewResponseDto.builder()
                .itemId(item.getItemId())
                .categoryName(item.getCategoryId().getName())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .content(item.getContent())
                .build();
    }
}
