package com.backend.martall.domain.itemlike.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(name = "mart_item_id")
    private int martItemId;

    @Column(name = "user_idx")
    private Long userIdx;
}