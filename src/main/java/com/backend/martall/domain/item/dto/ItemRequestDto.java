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
public class ItemRequestDto {

    private int itemId;
    private String martShopId;
    private ItemCategory categoryId;
    private String itemName;
    private int price;
    private int inventoryQuantity;
    private LocalDateTime regDatetime;
    private String content;

    public Item toEntity() {
        return Item.builder()
                .itemId(this.itemId)
                .martShopId(this.martShopId)
                .categoryId(this.categoryId)
                .itemName(this.itemName)
                .price(this.price)
                .inventoryQuantity(this.inventoryQuantity)
                .regDatetime(this.regDatetime)
                .content(this.content)
                .build();
    }
}
