package com.backend.martall.domain.cart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemUpdateRequestDto {
    private Long cartItemId;
    private int cartItemCount;
}
