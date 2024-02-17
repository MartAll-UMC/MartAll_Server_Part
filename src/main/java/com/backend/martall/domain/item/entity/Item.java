package com.backend.martall.domain.item.entity;

import com.backend.martall.domain.BaseTime;
import com.backend.martall.domain.itemlike.entity.ItemLike;
import com.backend.martall.domain.mart.entity.MartShop;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mart_shop_id")
    private MartShop martShop;

    @Enumerated(EnumType.STRING)
    @Column(name = "item_category_id")
    private ItemCategory categoryId;

    @Column(name = "item_name", length = 50)
    private String itemName;

    @Column(name = "price")
    private int price;

    @Column(name = "inventory_quantity")
    private int inventoryQuantity;

    @Column(name = "content", length = 500)
    private String content;

    @Column(name = "profile_photo", length = 255)
    private String profilePhoto;

    @OneToMany(mappedBy = "picItem", fetch = FetchType.LAZY)
    private List<ItemPic> itemPicList = new ArrayList<>();

    @OneToMany(mappedBy = "item", fetch = FetchType.LAZY)
    private List<ItemLike> itemLikeList = new ArrayList<>();

    public void addPic(ItemPic itemPic) {
        this.itemPicList.add(itemPic);
        itemPic.setPicItem(this);
    }

    public void deletePic(ItemPic itemPic) {
        this.itemPicList.remove(itemPic);
    }

    public void addLike(ItemLike itemLike) {
        this.itemLikeList.add(itemLike);
    }

    public void deleteLike(ItemLike itemLike) {
        this.itemLikeList.remove(itemLike);
    }

    public void setPic(String profileUrl, String contentUrl) {
        this.profilePhoto = profileUrl;
        this.content = contentUrl;
    }
}
