package com.backend.martall.domain.itemlike.repository;

import com.backend.martall.domain.itemlike.entity.ItemLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemLikeRepository extends JpaRepository<ItemLike, Integer> {

    boolean existsByUserIdxAndMartItemId(Long userIdx, int martItemId);
    void deleteByUserIdxAndMartItemId(Long userIdx, int martItemId);
    List<ItemLike> findByUserIdx(Long userIdx);
}
