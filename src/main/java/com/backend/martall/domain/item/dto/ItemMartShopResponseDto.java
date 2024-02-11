package com.backend.martall.domain.item.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
// 상품 상세정보에 마트 정보를 넣어야 해서 추가했습니다!
public class ItemMartShopResponseDto {
    private Long martShopId;
    private String martName;
    private List<Object> martCategory;
    private int bookmarkCount;
    private int likeCount;
}
