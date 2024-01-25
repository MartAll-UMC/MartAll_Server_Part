package com.backend.martall.domain.order.entity;

import com.backend.martall.domain.BaseTime;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Order extends BaseTime {

    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @Column(name = "martshop_id")
    private String martshopId;

    @Column(name = "user_idx")
    private Long userIdx;

    @Column(name = "order_state")
    private String orderState;

    @Column(name = "payment_type")
    private String paymentType;
}
