package com.backend.martall.domain.order.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderInquiryResponse {
    private OrderInfoResponse order;

    private List<OrderItemResponse> orderItem;

}
