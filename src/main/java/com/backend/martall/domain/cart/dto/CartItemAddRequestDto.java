package com.backend.martall.domain.cart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemAddRequestDto {
    private int itemId;
    private int cartItemCount;
}
