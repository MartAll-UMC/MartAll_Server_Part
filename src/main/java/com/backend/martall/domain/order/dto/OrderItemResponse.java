package com.backend.martall.domain.order.dto;

import com.backend.martall.domain.order.entity.OrderInfo;
import com.backend.martall.domain.order.entity.OrderItem;
import com.backend.martall.domain.order.entity.OrderState;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {

    private Long orderItemId;

    private int itemId;

    private String martShopId;

    private String martName;

    private String picName;

    private String orderState;

    private String itemName;

    private int count;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderTime;

    public static OrderItemResponse of (OrderItem orderItem, OrderInfo orderInfo) {
        return OrderItemResponse.builder()
                .orderItemId(orderItem.getOrderItemId())
                .itemId(orderItem.getItemId())
                .martShopId(orderInfo.getMartShopId())
                .orderState(OrderState.getStateByCode(orderInfo.getOrderState()))
                .count(orderItem.getCount())
                .build();
    }

}
