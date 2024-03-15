package com.backend.martall.domain.cart.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartInquiryResponseDto {

    private Mart mart;

    private List<CartItem> items;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Mart {
        private Long martId;
        private String martName;
        private List<String> martCategory;
        private int bookmarkCount;
        private int likeCount;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CartItem {
        private Long cartItemId;
        private int itemId;
        private String itemImg;
        private String itemName;
        private int itemPrice;
        private String itemCategory;
        private int cartItemCount;
    }
}
