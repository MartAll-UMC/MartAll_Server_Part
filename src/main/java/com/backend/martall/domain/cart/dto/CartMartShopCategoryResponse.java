package com.backend.martall.domain.cart.dto;

import com.backend.martall.domain.mart.entity.MartCategory;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartMartShopCategoryResponse {
    private String categoryName;
    private int index;

    public static CartMartShopCategoryResponse of (MartCategory martCategory) {
        return CartMartShopCategoryResponse.builder()
                .categoryName(martCategory.getCategoryName())
                .index(martCategory.getCategoryIndex())
                .build();
    }
}
