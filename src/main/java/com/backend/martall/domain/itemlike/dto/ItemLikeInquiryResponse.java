package com.backend.martall.domain.itemlike.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemLikeInquiryResponse {
    private List<ItemLikeResponse> item;
}
