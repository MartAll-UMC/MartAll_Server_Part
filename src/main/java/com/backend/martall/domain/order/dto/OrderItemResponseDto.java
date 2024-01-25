package com.backend.martall.domain.order.dto;

import com.backend.martall.domain.order.entity.Order;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponseDto {

    private Long orderItemId;

    private int itemId;

    private Order order;

    private int number;
}
