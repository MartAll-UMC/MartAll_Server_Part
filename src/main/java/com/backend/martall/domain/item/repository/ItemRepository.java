package com.backend.martall.domain.item.repository;

import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.item.entity.ItemCategory;
import com.backend.martall.domain.mart.entity.MartShop;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query("SELECT item FROM Item item WHERE item.itemName LIKE %:itemName% OR item.content LIKE %:itemName%")
    List<Item> searchByItemName(String itemName);

//    @Query("SELECT item FROM Item item WHERE item.itemId = :itemId AND item.martShopId = :shopId")
//    Item findByShopIdAndItemId(Long shopId, int itemId);
//

    @Query("SELECT item FROM Item item WHERE (:itemCategory = 'ALL' OR item.categoryId = :itemCategory) " +
            "AND (:minPrice IS NULL OR item.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR item.price <= :maxPrice) " )
    List<Item> searchByCategoryAndPrice(ItemCategory itemCategory, Integer minPrice, Integer maxPrice);

    @Query("SELECT item FROM Item item WHERE (:itemCategory = 'ALL' OR item.categoryId = :itemCategory) " +
            "AND (:minPrice IS NULL OR item.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR item.price <= :maxPrice) " +
            "ORDER BY item.createdAt DESC")
    List<Item> searchByCategoryAndPriceCreatedAtDesc(ItemCategory itemCategory, Integer minPrice, Integer maxPrice);

    @Query("SELECT item FROM Item item WHERE (:itemCategory = 'ALL' OR item.categoryId = :itemCategory) " +
            "AND (:minPrice IS NULL OR item.price >= :minPrice) " +
            "AND (:maxPrice IS NULL OR item.price <= :maxPrice) " +
            "ORDER BY SIZE(item.itemLikeList) DESC")
    List<Item> searchByCategoryAndPriceLikeDesc(ItemCategory itemCategory, Integer minPrice, Integer maxPrice);


    Optional<Item> findByItemId(int itemId);

    List<Item> findTop8ByOrderByCreatedAtDesc(); //"createdAt" 컬럼을 기준으로 내림차순으로 정렬된 최대 8개의 항목을 가져옴

    List<Item> findByMartShopOrderByCreatedAtDesc(MartShop martShop);

    @Query("SELECT item FROM Item item ORDER BY FUNCTION('RAND')")
    List<Item> findRandomItem(Pageable pageable);
}
