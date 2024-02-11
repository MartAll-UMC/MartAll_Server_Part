package com.backend.martall.domain.item.dto;

import com.backend.martall.domain.item.entity.Item;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// API 명세서에서 필요한걸 추가했습니다!
// (상품 사진과 마트 이름 추가), 카테고리 id도 이름으로 바꾸는게 좋아보여서 바꿨습니다!
// 검색하거나 마트 상세정보에서 나오는 상품 DTO 입니다
// 이름은 잘 수정해주세요 ㅋㅋ...
public class ItemListResponseDto {

    private int itemId;
    private String pic;
    private String categoryName;
    private String martShopName;
    private String itemName;
    private int price;
    private String content;

    public static ItemListResponseDto from(Item item) {
        return ItemListResponseDto.builder()
                .itemId(item.getItemId())
                .categoryName(item.getCategoryId().getName())
                .itemName(item.getItemName())
                .price(item.getPrice())
                .content(item.getContent())
                .build();
    }
}
