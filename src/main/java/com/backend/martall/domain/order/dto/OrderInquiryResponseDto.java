package com.backend.martall.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
        @Schema(example = "1")
        private Long martId;
        @Schema(example = "바나나올")
        private String martName;
        @Schema(example = "3")
        private int orderItemCount;
        @Schema(example = "259000")
        private int payment;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class OrderItem {
        @Schema(example = "24")
        private Long orderItemId;
        @Schema(example = "1")
        private int itemId;
        @Schema(example = "상품 이미지 경로")
        private String itemImg;
        @Schema(example = "황금 바나나")
        private String itemName;
        @Schema(example = "77777")
        private int itemPrice;
        @Schema(example = "과일&채소")
        private String itemCategory;
        @Schema(example = "2")
        private int orderItemCount;
    }

}
