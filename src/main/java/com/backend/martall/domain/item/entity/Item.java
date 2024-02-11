package com.backend.martall.domain.item.entity;

import com.backend.martall.domain.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Table(name = "mart_item")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Item extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private int itemId;

    @Column(name = "mart_shop_id", length = 50)
    private String martShopId;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_category_id")
    private ItemCategory categoryId;

    @Column(name = "item_name", length = 50)
    private String itemName;

    @Column(name = "price")
    private int price;

    @Column(name = "inventory_quantity")
    private int inventoryQuantity;

    @Column(name = "reg_datetime")
    private LocalDateTime regDatetime;

    @Column(name = "content", length = 500)
    private String content;

    @Column(name = "profile_photo", length = 255)
    private String profilePhoto;
}
