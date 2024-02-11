package com.backend.martall.domain.item.repository;

import com.backend.martall.domain.item.dto.ItemResponseDto;
import com.backend.martall.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT item FROM Item item WHERE item.itemName LIKE %:itemName% OR item.content LIKE %:itemName%")
    List<Item> findByItemName(String itemName);

    @Query("SELECT item FROM Item item WHERE item.itemId = :itemId AND item.martShopId = :shopId")
    Item findById(Long shopId, int itemId);

    // 밑에 쿼리문으로 대체했습니다!
//    @Query("SELECT item FROM Item item ORDER BY item.createdAt DESC")
//    List<Item> findAllOrderByCreatedAtDesc();

    List<Item> findTop8ByOrderByCreatedAtDesc();
}
