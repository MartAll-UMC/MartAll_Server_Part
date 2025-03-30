package com.backend.martall.domain.item.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetailResponseDto {

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
    @Schema(example = "아이템 정보 이미지 경로")
    private String itemContentImg;
    private String itemCategory;
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
        @ArraySchema(schema = @Schema(example = "식품"))
        private List<String> martCategory;
        @Schema(example = "123")
        private int bookmarkCount;
        @Schema(example = "321")
        private int likeCount;
        @Schema(example = "true")
        private boolean martBookmark;
    }
}
