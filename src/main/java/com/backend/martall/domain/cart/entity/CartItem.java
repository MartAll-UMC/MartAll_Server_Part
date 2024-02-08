package com.backend.martall.domain.cart.entity;

import com.backend.martall.domain.cart.dto.CartItemRequest;
import com.backend.martall.domain.user.entity.User;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;
//    @Column(name = "user_idx")
//    private Long userIdx;

    @Column(name = "item_id")
    private int itemId;

    @Column(name = "count")
    private int count;

    public void updateCartItem(CartItemRequest cartRequestDto) {
        this.count = cartRequestDto.getCount();
    }
}
