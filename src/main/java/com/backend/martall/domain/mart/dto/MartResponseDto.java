package com.backend.martall.domain.mart.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MartResponseDto {
    private Long martId;
    private String martImg;
    private String martName;
    private List<String> martCategory;
    private int bookmarkCount;
    private int likeCount;
    private boolean martBookmark;
}
