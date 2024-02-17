package com.backend.martall.domain.item.dto;

import com.backend.martall.domain.item.entity.Item;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemMartNewResponseDto {
    private int itemId;
    private String name;
    private String imageUrl;
    private int price;
    private boolean likeYn;

    public static ItemMartNewResponseDto of(Item item) {
        return ItemMartNewResponseDto.builder()
                .itemId(item.getItemId())
                .name(item.getItemName())
                .imageUrl(item.getProfilePhoto())
                .price(item.getPrice())
                .build();
    }
}
