package com.backend.martall.domain.order.dto;

import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.order.entity.OrderItem;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponse {

    private Long orderItemId;

    private int itemId;

    private String categoryName;

    private String picName;

    private String itemName;

    private int count;

    private int price;

    public static OrderItemResponse of (OrderItem orderItem) {
        return OrderItemResponse.builder()
                .orderItemId(orderItem.getOrderItemId())
                .itemId(orderItem.getItem().getItemId())
                .categoryName(orderItem.getItem().getCategoryId().getName())
                .picName(orderItem.getItem().getProfilePhoto())
                .itemName(orderItem.getItem().getItemName())
                .count(orderItem.getCount())
                .price(orderItem.getItem().getPrice())
                .build();
    }

}
