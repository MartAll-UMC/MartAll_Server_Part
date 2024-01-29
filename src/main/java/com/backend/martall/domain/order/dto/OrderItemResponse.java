package com.backend.martall.domain.order.dto;

import com.backend.martall.domain.order.entity.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderItemResponse {

    private Long orderItemId;

    private int itemId;

    private String martShopId;

    private String martName;

    private String picName;

    private String orderState;

    private String itemName;

    private int count;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderTime;

}
