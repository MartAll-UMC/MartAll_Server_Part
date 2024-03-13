package com.backend.martall.domain.mart.dto;

import com.backend.martall.domain.item.dto.ItemMartNewResponseDto;
import com.backend.martall.domain.itemlike.service.ItemLikeService;
import com.backend.martall.domain.mart.entity.MartCategory;
import com.backend.martall.domain.mart.entity.MartShop;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

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
