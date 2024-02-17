package com.backend.martall.domain.mart.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryCreateRequestDto {
    private Long martShopId;
    private String categoryName;
}
