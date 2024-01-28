package com.backend.martall.domain.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartInquiryResponse {

//    private List<mart~~> -> 마트 리스트 추가

    private List<CartItemResponse> cartItemResponseList;
}
