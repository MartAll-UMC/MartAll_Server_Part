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

    @Query("SELECT m FROM MartShop m LEFT JOIN m.martCategories c WHERE (:tag = '전체' OR c.categoryName = :tag)" +
            "AND (:minBookmark IS NULL OR SIZE(m.martBookmarks) >= :minBookmark)" +
            "AND (:maxBookmark IS NULL OR SIZE(m.martBookmarks) <= :maxBookmark)" +
            "AND (:minLike IS NULL OR (SELECT count(il) FROM ItemLike il WHERE il.item.martShop = m) >= :minLike)" +
            "AND (:maxLike IS NULL OR (SELECT count(il) FROM ItemLike il WHERE il.item.martShop = m) <= :maxLike)")
    List<MartShop> searchByFilter(String tag, Integer minBookmark, Integer maxBookmark, Integer minLike, Integer maxLike);

    @Query("SELECT m FROM MartShop m LEFT JOIN m.martCategories c WHERE (:tag = '전체' OR c.categoryName = :tag)" +
            "AND (:minBookmark IS NULL OR SIZE(m.martBookmarks) >= :minBookmark)" +
            "AND (:maxBookmark IS NULL OR SIZE(m.martBookmarks) <= :maxBookmark)" +
            "AND (:minLike IS NULL OR (SELECT count(il) FROM ItemLike il WHERE il.item.martShop = m) >= :minLike)" +
            "AND (:maxLike IS NULL OR (SELECT count(il) FROM ItemLike il WHERE il.item.martShop = m) <= :maxLike)" +
            "ORDER BY m.createdAt DESC")
    List<MartShop> searchByFilterCreatedAtDesc(String tag, Integer minBookmark, Integer maxBookmark, Integer minLike, Integer maxLike);

    @Query("SELECT m FROM MartShop m LEFT JOIN m.martCategories c WHERE (:tag = '전체' OR c.categoryName = :tag)" +
            "AND (:minBookmark IS NULL OR SIZE(m.martBookmarks) >= :minBookmark)" +
            "AND (:maxBookmark IS NULL OR SIZE(m.martBookmarks) <= :maxBookmark)" +
            "AND (:minLike IS NULL OR (SELECT count(il) FROM ItemLike il WHERE il.item.martShop = m) >= :minLike)" +
            "AND (:maxLike IS NULL OR (SELECT count(il) FROM ItemLike il WHERE il.item.martShop = m) <= :maxLike)" +
            "ORDER BY SIZE(m.martBookmarks) DESC")
    List<MartShop> searchByFilterBookmarkDesc(String tag, Integer minBookmark, Integer maxBookmark, Integer minLike, Integer maxLike);

    @Query("SELECT m FROM MartShop m LEFT JOIN m.martCategories c WHERE (:tag = '전체' OR c.categoryName = :tag)" +
            "AND (:minBookmark IS NULL OR SIZE(m.martBookmarks) >= :minBookmark)" +
            "AND (:maxBookmark IS NULL OR SIZE(m.martBookmarks) <= :maxBookmark)" +
            "AND (:minLike IS NULL OR (SELECT count(il) FROM ItemLike il WHERE il.item.martShop = m) >= :minLike)" +
            "AND (:maxLike IS NULL OR (SELECT count(il) FROM ItemLike il WHERE il.item.martShop = m) <= :maxLike)")
    List<MartShop> searchByFilterLikeDesc(String tag, Integer minBookmark, Integer maxBookmark, Integer minLike, Integer maxLike);
//    findTop8ByOrderByCreatedAtDesc

    @Query(nativeQuery = true, value = "SELECT * FROM mart_shop m ORDER BY RAND() LIMIT 8")
    List<MartShop> findRandomMart();
}





