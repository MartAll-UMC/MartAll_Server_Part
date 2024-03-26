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
public class MartDetailResponseDto {

    @Schema(example = "1")
    private Long martId;
    @Schema(example = "마트 이미지 경로")
    private String martImg;
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
    @Schema(example = "마트 주소")
    private String martAddress;
    @Schema(example = "사장님 이름")
    private String martOwner;
    @Schema(example = "02-xxxx-xxxx")
    private String martNumber;
    @Schema(example = "월-금 09:00 - 18:00")
    private String martOperationTime;


}
