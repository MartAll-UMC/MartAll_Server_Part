package com.backend.martall.domain.order.entity;

import com.backend.martall.domain.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfo extends BaseTime {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(name = "martshop_id")
    private String martShopId;

    @Column(name = "user_idx")
    private Long userIdx;

    @Column(name = "order_state")
    private String orderState;

    @Column(name = "payment_type")
    private String paymentType;

    public void updateOrderState(OrderState orderState) {
        this.orderState = orderState.getCode();
    }
}
