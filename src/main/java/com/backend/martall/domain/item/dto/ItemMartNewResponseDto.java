package com.backend.martall.domain.item.dto;

import com.backend.martall.domain.item.entity.Item;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemMartNewResponseDto {
    private int itemId;
    private String itemName;
    private String itemImg;
    private int itemPrice;
    private boolean itemLike;
    private Mart mart;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Mart {
        private Long martId;
        private String martName;
    }
}
