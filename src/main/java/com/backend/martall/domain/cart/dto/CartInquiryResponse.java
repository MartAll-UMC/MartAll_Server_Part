package com.backend.martall.domain.cart.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartInquiryResponse {

//    private cartMartShopResponse mart

    private List<CartItemResponse> cartItemResponseList;
}
