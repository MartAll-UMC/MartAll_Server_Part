package com.backend.martall.domain.item.dto;

import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.item.entity.ItemCategory;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ItemAddRequestDto {

    private int itemId;
    private Long martShopId;
    private ItemCategory categoryId;
    private String itemName;
    private int price;
    private int inventoryQuantity;
    private String content;
    private String profilePhoto;

    public Item toEntity() {
        return Item.builder()
                .itemId(this.itemId)
                .martShopId(this.martShopId)
                .categoryId(this.categoryId)
                .itemName(this.itemName)
                .price(this.price)
                .inventoryQuantity(this.inventoryQuantity)
                .content(this.content)
                .profilePhoto(this.profilePhoto)
                .build();
    }
}
