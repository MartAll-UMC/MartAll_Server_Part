package com.backend.martall.domain.itemlike.repository;

import com.backend.martall.domain.itemlike.entity.ItemLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemLikeRepository extends JpaRepository<ItemLike, Integer> {

}
