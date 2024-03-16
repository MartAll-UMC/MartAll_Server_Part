package com.backend.martall.domain.order.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderInquiryResponseDto {
    private OrderInfo order;

    private List<OrderItem> items;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderInfo {
        private Long martId;
        private String martName;
        private int orderItemCount;
        private int payment;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItem {
        private Long orderItemId;
        private int itemId;
        private String itemImg;
        private String itemName;
        private int itemPrice;
        private String itemCategory;
        private int orderItemCount;
    }

}
