package com.backend.martall.domain.itemlike.repository;

import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.itemlike.entity.ItemLike;
import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemLikeRepository extends JpaRepository<ItemLike, Integer> {

    boolean existsByUserAndItem(User user, Item item);
    void deleteByUserAndItem(User user, Item item);
    List<ItemLike> findByUser(User user);

    // 마트에 있는 상품의 찜하기 수를 count
    @Query("SELECT count(il) FROM ItemLike il WHERE il.item.martShop = :martShop")
    int countItemLikeByMart(MartShop martShop);

}
