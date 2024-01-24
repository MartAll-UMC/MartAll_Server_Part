package com.backend.martall.domain.cart.dto;

import com.backend.martall.domain.cart.entity.CartItem;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartResponseDto {

    private Long cartItemId;

    private int itemId;

    private int number;

    public static CartResponseDto of (CartItem cartItem) {
        return CartResponseDto.builder()
                .cartItemId(cartItem.getCartItemId())
                .itemId(cartItem.getItemId())
                .number(cartItem.getNumber())
                .build();
    }
}
