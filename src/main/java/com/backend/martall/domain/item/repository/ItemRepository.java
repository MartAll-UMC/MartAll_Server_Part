package com.backend.martall.domain.item.repository;

import com.backend.martall.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    @Query("SELECT item FROM Item item WHERE item.itemName LIKE %:itemName% OR item.content LIKE %:itemName%")
    List<Item> findByItemName(String itemName);
}
