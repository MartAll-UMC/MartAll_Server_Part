package com.backend.martall.domain.item.repository;

import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.item.entity.ItemPic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ItemPicRepository extends JpaRepository<ItemPic, Integer> {

    //picItem과 picIndex가 주어진 조건을 만족하는 경우 해당하는 ItemPic 엔티티를 반환
    //해당하는 결과가 없는 경우에는 빈 Optional을 반환
    Optional<ItemPic> findByPicItemAndPicIndex(Item picItem, int picIndex);
}
