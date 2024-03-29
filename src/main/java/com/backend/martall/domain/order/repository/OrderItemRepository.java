package com.backend.martall.domain.order.repository;

import com.backend.martall.domain.order.entity.OrderInfo;
import com.backend.martall.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderInfo(OrderInfo orderInfo);
}
