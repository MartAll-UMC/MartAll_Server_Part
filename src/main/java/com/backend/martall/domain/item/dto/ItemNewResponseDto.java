package com.backend.martall.domain.item.dto;

import com.backend.martall.domain.item.entity.Item;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 이건 새로 나온 상품 관련 Dto 입니다
// 사진 경로랑 카테고리 id -> 이름으로 추가 했어요!
public class ItemNewResponseDto {
    private int itemId;
    private String pic;
    private String categoryName;
    private String itemName;
    private int price;
    private String content;

    public static ItemNewResponseDto from(Item item) {
        return ItemNewResponseDto.builder()
                .itemId(item.getItemId())
                .categoryName(item.getCategoryId().getName())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .content(item.getContent())
                .build();
    }
}
