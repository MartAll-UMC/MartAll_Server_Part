package com.backend.martall.domain.item.repository;

import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.mart.entity.MartShop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query("SELECT item FROM Item item WHERE item.itemName LIKE %:itemName% OR item.content LIKE %:itemName%")
    List<Item> findByItemName(String itemName);

//    @Query("SELECT item FROM Item item WHERE item.itemId = :itemId AND item.martShopId = :shopId")
//    Item findByShopIdAndItemId(Long shopId, int itemId);
//
    Optional<Item> findByMartShopAndItemId(MartShop martShop, int itemId);

    List<Item> findTop8ByOrderByCreatedAtDesc(); //"createdAt" 컬럼을 기준으로 내림차순으로 정렬된 최대 8개의 항목을 가져옴
}
