package com.backend.martall.domain.itemlike.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemLikeResponse {
    private int itemId;
    private String picName;
    private String itemName;
    private int price;
    private Long martShopId;
    private String martName;
    private boolean like;
}