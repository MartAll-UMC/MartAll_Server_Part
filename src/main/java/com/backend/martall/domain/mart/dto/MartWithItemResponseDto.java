package com.backend.martall.domain.mart.dto;

import com.backend.martall.domain.item.dto.ItemMartNewResponseDto;
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
    private List<ItemMartNewResponseDto> items;

}
