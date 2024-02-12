package com.backend.martall.domain.order.repository;

import com.backend.martall.domain.order.entity.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderInfoRepository extends JpaRepository<OrderInfo, Long> {
    Optional<OrderInfo> findByUserIdxAndOrderState(Long userIdx, String orderState);

    boolean existsByUserIdxAndOrderState(Long userIdx, String orderState);

}
