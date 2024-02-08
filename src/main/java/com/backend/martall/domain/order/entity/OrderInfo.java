package com.backend.martall.domain.order.entity;

import com.backend.martall.domain.BaseTime;
import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.user.entity.User;
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
@Table(name = "order_info")
public class OrderInfo extends BaseTime {
    @Id
    @Column(name = "order_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "martshop_id")
    private MartShop martShop;
//    @Column(name = "martshop_id")
//    private String martShopId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;
//    @Column(name = "user_idx")
//    private Long userIdx;

    @Column(name = "order_state")
    private String orderState;

    @Column(name = "payment_type")
    private String paymentType;

    public void updateOrderState(OrderState orderState) {
        this.orderState = orderState.getCode();
    }
}
