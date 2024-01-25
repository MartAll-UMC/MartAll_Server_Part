package com.backend.martall.domain.order.repository;

import com.backend.martall.domain.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.beans.JavaBean;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
