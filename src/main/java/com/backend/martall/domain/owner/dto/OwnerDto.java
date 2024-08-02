package com.backend.martall.domain.owner.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Getter
    public static class OrderDetailRequestDto {
        private Long orderId;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderDetailResponseDto {
        private String customerName;
        private Boolean regularState;
        private LocalDateTime orderAt;
        private List<Item> itemList;
        private Integer sumPrice;

        @Getter
        @Setter
        @Builder
        @AllArgsConstructor
        @NoArgsConstructor
        public static class Item {
            private String productName;
            private Integer quantity;
            private Integer price;
        }
    }


    @Getter
    @Setter
    public static class ItemCreateRequestDto {

        @NotNull
        @NotBlank
        private String itemName;

        @NotNull
        @NotBlank
        private String itemCategory;

        @NotNull
        @Min(0)
        private Integer price;
    }

    @Getter
    @Setter
    public static class ItemUpdateRequestDto {
        private Integer itemId;

        private String itemName;

        private String itemCategory;

        @Min(0)
        private Integer price;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemResponseDto {
        private Integer itemId;
    }

    @Getter
    public static class MartExposureRequestDto {
        private Boolean exposure;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MartExposureResponseDto {
        private Boolean exposure;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MartMainResponseDto {
        private Long martId;
        private String martName;
        private Boolean exposure;
        private List<String> martCategory;
        private Integer bookmarkCount;
        private Integer likeCount;
    }
}
