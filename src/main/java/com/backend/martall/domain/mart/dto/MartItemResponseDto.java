package com.backend.martall.domain.mart.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MartItemResponseDto {
    @Schema(example = "1")
    private int itemId;
    @Schema(example = "상품 이미지")
    private String itemImg;
    @Schema(example = "황금 바나나")
    private String itemName;
    @Schema(example = "77777")
    private int itemPrice;
    @Schema(example = "true")
    private boolean itemLike;
    private Mart mart;

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
    }
}
