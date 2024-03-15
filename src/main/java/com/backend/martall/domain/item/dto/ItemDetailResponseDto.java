package com.backend.martall.domain.item.dto;

import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.item.entity.ItemCategory;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetailResponseDto {

    private int itemId;
    private String itemImg;
    private String itemName;
    private int itemPrice;
    private boolean itemLike;
    private String itemContentImg;
    private Mart mart;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Mart {
        private Long martId;
        private String martName;
        private List<String> martCategory;
        private int bookmarkCount;
        private int likeCount;
        private boolean martBookmark;
    }
}
