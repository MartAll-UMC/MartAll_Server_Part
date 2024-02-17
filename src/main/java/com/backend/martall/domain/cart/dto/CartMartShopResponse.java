package com.backend.martall.domain.cart.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartMartShopResponse {
    private Long martShopId;
    private String martName;
    private List<CartMartShopCategoryResponse> martCategory;
    private int bookmarkCount;
    private int likeCount;
}
