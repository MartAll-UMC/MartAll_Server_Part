package com.backend.martall.domain.order.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateRequestDto {

    private int payment;

    private Long martId;

    private List<CartItem> cartItems;

    @Getter
    @Setter
    public static class CartItem {
        private Long cartItemId;
    }
}
