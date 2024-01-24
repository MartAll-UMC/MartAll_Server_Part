package com.backend.martall.domain.cart.entity;

import com.backend.martall.domain.cart.dto.CartRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "cart_item")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Long cartItemId;

    @Column(name = "user_idx")
    private Long userIdx;

    @Column(name = "item_id")
    private int itemId;

    @Column(name = "number")
    private int number;

    public void updateCartItem(CartRequestDto cartRequestDto) {
        this.number = cartRequestDto.getNumber();
    }
}
