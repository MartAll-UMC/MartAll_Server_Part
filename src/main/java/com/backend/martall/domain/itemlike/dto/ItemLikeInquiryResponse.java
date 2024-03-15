package com.backend.martall.domain.itemlike.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemLikeInquiryResponse {
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

