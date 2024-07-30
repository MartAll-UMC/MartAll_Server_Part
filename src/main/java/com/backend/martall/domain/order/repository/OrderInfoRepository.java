package com.backend.martall.domain.order.repository;

import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.order.entity.OrderInfo;
import com.backend.martall.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderInfoRepository extends JpaRepository<OrderInfo, Long> {
    Optional<OrderInfo> findByUserAndOrderState(User user, String orderState);

    boolean existsByUserAndOrderState(User user, String orderState);

    Optional<OrderInfo> findByUser(User user);

    List<OrderInfo> findByMartShopAndOrderState(MartShop martShop, String orderState);

    @Query("SELECT e FROM OrderInfo e WHERE (e.orderState = 'C' AND FUNCTION('DATE', e.createdAt) = current date) " +
            "AND (e.martShop = :martShop)")
    List<OrderInfo> findTodayCompleteByMartShop(MartShop martShop);

    Long countByMartShopAndOrderState(MartShop martShop, String orderState);

    @Modifying
    @Query("UPDATE OrderInfo o SET o.orderState = :state WHERE o.orderId = :orderId")
    Integer updateStateById(Long orderId, String state);

}
