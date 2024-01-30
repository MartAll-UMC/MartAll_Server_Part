package com.backend.martall.domain.order.repository;

import com.backend.martall.domain.order.entity.OrderInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderInfoRepository extends JpaRepository<OrderInfo, Long> {
    List<OrderInfo> findByUserIdx(Long userIdx);
    List<OrderInfo> findByUserIdxAndOrderState(Long userIdx, String orderState);

    OrderInfo findByUserIdxAndMartShopIdAndOrderState(Long userIdx, String martShopId, String orderState);
}
