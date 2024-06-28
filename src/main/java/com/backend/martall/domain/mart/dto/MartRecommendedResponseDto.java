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
public class MartRecommendedResponseDto {
    @Schema(example = "1")
    private Long martId;
    @Schema(example = "마트 이미지 경로")
    private String martImg;
    @Schema(example = "바나나올")
    private String martName;
    @ArraySchema(schema = @Schema(example = "식품"))
    private List<String> category;
}
