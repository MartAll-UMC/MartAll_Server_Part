package com.backend.martall.domain.cart.dto;

import com.backend.martall.domain.cart.entity.CartItem;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemResponse {

    private Long cartItemId;

    private int itemId;

    private String categoryName;

    private String picName;

    private int count;

    private String itemName;

    private int price;

    public static CartItemResponse of (CartItem cartItem) {
        return CartItemResponse.builder()
                .cartItemId(cartItem.getCartItemId())
                .itemId(cartItem.getItem().getItemId())
                .count(cartItem.getCount())
                .build();
    }
}
