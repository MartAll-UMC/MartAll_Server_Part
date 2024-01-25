package com.backend.martall.domain.order.service;

import com.backend.martall.domain.order.entity.OrderItem;
import com.backend.martall.domain.order.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserOrderService {

    private final OrderRepository orderRepository;

    // 주문 생성
    // 사용자 아이디의 장바구니 상품 -> 가게별로 분류 -> 같은 가게의 상품끼리 주문 생성
    @Transactional
    public void createOrder(Long userIdx) {

    }

//    public List<OrderItem> sortOrderItem() {
//
//    }

}
