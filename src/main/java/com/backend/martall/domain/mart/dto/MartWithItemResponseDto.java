package com.backend.martall.domain.mart.dto;

import com.backend.martall.domain.item.dto.ItemMartNewResponseDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MartWithItemResponseDto {
    private Long martId;
    private String martName;
    private List<String> martCategory;
    private int bookmarkCount;
    private int likeCount;
    private boolean isBookmark;
    private List<ItemMartNewResponseDto> items;

}
