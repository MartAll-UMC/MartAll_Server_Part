package com.backend.martall.domain.item.dto;

import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.item.entity.ItemCategory;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponseDto {

    private int itemId;
    private String martShopId;
    private ItemCategory categoryId;
    private String itemName;
    private int price;
    private int inventoryQuantity;
    private LocalDateTime regDatetime;
    private String content;
    private String profilePhoto;

    public static ItemResponseDto from(Item item) {
        return ItemResponseDto.builder()
                .itemId(item.getItemId())
                .categoryId(item.getCategoryId())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .inventoryQuantity(item.getInventoryQuantity())
                .regDatetime(item.getCreatedAt())
                .content(item.getContent())
                .profilePhoto(item.getProfilePhoto())
                .build();
    }
}
