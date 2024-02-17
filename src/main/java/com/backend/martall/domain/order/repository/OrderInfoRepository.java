package com.backend.martall.domain.order.repository;

import com.backend.martall.domain.order.entity.OrderInfo;
import com.backend.martall.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderInfoRepository extends JpaRepository<OrderInfo, Long> {
    Optional<OrderInfo> findByUserAndOrderState(User user, String orderState);

    Optional<OrderInfo> findByUser(User user);

}
