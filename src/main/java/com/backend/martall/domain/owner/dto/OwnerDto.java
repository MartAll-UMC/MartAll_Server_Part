package com.backend.martall.domain.owner.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class OwnerDto {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderListResponseDto {
        private Long wCount;
        private Long pCount;
        private Long cCount;
        private List<Order> order;

        @Getter
        @Setter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Order {
            private Long orderId;
            private String customerName;
            private Boolean regularState;
            private LocalDateTime orderAt;
            private String orderName;
            private Long sumPrice;
            private String state;
        }
    }

    @Getter
    public static class OrderStateUpdateRequestDto {
        private Long orderId;
        private String orderState;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderStateUpdateResponseDto {
        private Long orderId;
        private String orderState;
    }
}
