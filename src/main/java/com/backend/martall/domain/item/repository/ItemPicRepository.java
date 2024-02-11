package com.backend.martall.domain.item.repository;

import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.item.entity.ItemPic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemPicRepository extends JpaRepository<ItemPic, Integer> {
    Optional<ItemPic> findByPicItemAndPicIndex(Item picItem, int picIndex);
}
