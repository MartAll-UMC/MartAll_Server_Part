package com.backend.martall.domain.mart.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MartDetailResponseDto {

    private Long martId;
    private String martImg;
    private String martName;
    private List<String> martCategory;
    private int bookmarkCount;
    private int likeCount;
    private boolean isBookmark;
    private String martAddress;
    private String martOwner;
    private String martNumber;
    private String martOperationTime;

}
