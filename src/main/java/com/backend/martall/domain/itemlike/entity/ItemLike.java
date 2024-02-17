package com.backend.martall.domain.itemlike.entity;

import com.backend.martall.domain.item.entity.Item;
import com.backend.martall.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

@Table(name = "item_like")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_like_id")
    private int itemLikeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mart_item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;
}
