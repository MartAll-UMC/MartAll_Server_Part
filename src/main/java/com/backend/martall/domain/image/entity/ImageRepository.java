package com.backend.martall.domain.image.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    @Query("SELECT i.imgUrl FROM Image i WHERE i.category = :category AND i.categoryIdx = :categoryIdx")
    List<String> findUrlsByCategoryAndCategoryIdx(@Param("category") String category, @Param("categoryIdx") Long categoryIdx);

    @Transactional
    @Modifying
    @Query("DELETE FROM Image i WHERE i.imgUrl = :imgUrl")
    void deleteByImgUrl(@Param("imgUrl") String imgUrl);

    void deleteByCategoryAndCategoryIdx(String category, Long categoryIdx);
}
