package com.backend.martall.domain.item.dto;

import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.item.entity.ItemCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
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

    public static ItemResponseDto from(Item item) {
        ItemResponseDto itemResponseDto = new ItemResponseDto();

        itemResponseDto.itemId = item.getItemId();
        itemResponseDto.martShopId = item.getMartShopId();
        itemResponseDto.categoryId = item.getCategoryId();
        itemResponseDto.itemName = item.getItemName();
        itemResponseDto.price = item.getPrice();
        itemResponseDto.inventoryQuantity = item.getInventoryQuantity();
        itemResponseDto.regDatetime = item.getRegDatetime();
        itemResponseDto.content = item.getContent();

        return itemResponseDto;
    }
}
