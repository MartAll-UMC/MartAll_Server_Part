package com.backend.martall.domain.cart.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartInquiryResponseDto {

    private Mart mart;

    private List<CartItem> items;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Mart {
        @Schema(example = "1")
        private Long martId;
        @Schema(example = "바나나올")
        private String martName;
        @ArraySchema(schema = @Schema(example = "식품"))
        private List<String> martCategory;
        @Schema(example = "123")
        private int bookmarkCount;
        @Schema(example = "321")
        private int likeCount;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CartItem {
        @Schema(example = "5")
        private Long cartItemId;
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
        private int cartItemCount;
    }
}
