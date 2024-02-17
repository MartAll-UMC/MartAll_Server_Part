package com.backend.martall.domain.mart.dto;

import com.backend.martall.domain.item.dto.ItemMartNewResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MartSearchResponseDto {
    private Long martShopId;
    private String photo;
    private String name;
    private List<String> martcategory;
    private int followersCount;
    private int visitorsCount; // = likecount
    private boolean isFavorite;
}
