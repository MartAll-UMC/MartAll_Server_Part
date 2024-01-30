package com.backend.martall.domain.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderInquiryResponse {
    private List<OrderItemResponse> orderItem;

    public void addOrderItem(List<OrderItemResponse> addList) {
        this.orderItem.addAll(addList);
    }
}
