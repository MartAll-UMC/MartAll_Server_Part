package com.backend.martall.domain.itemlike.repository;

import com.backend.martall.domain.itemlike.entity.ItemLike;
import com.backend.martall.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemLikeRepository extends JpaRepository<ItemLike, Integer> {

    boolean existsByUserAndMartItemId(User user, int martItemId);
    void deleteByUserAndMartItemId(User user, int martItemId);
    List<ItemLike> findByUser(User user);
}
