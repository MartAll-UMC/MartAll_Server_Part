package com.backend.martall.domain.mart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.backend.martall.domain.mart.entity.MartShop;
import java.util.List;

@Repository
public interface MartRepository extends JpaRepository<MartShop, Long> {


    @Query("SELECT m FROM MartShop m WHERE m.name LIKE %:keyword% OR m.introduction LIKE %:keyword%")
    List<MartShop> findByKeyword(String keyword);

    @Query("SELECT m FROM MartShop m WHERE (:category IS NULL OR m.category = :category) AND (:rating IS NULL OR m.rating >= :rating)")
    List<MartShop> findByCategoryAndRating(String category, Double rating);
}


