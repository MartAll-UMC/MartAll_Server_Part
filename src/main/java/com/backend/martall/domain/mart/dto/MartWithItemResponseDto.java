package com.backend.martall.domain.mart.dto;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MartWithItemResponseDto {
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
    private List<ItemDto> items;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ItemDto {
        private int itemId;
        private String itemImg;
        private String itemName;
        private int itemPrice;
        private boolean itemLike;

    }
}
