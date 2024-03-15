package com.backend.martall.domain.item.dto;

import com.backend.martall.domain.item.entity.Item;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemNewResponseDto {
    private int itemId;
    private String itemImg;
    private String itemName;
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
