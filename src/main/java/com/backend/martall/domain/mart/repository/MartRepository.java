package com.backend.martall.domain.mart.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.backend.martall.domain.mart.entity.MartShop;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


@Repository
public interface MartRepository extends JpaRepository<MartShop, Long> {


    @Query("SELECT m FROM MartShop m WHERE m.name LIKE %:keyword% OR m.introduction LIKE %:keyword%")
    List<MartShop> findByKeyword(String keyword);

    @Query("SELECT m FROM MartShop m JOIN m.martCategories c WHERE :categoryName IS NULL OR c.categoryName = :categoryName")
    List<MartShop> findByCategoryName(@Param("categoryName") String categoryName);
}





