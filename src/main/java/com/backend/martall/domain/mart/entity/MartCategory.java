package com.backend.martall.domain.mart.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "mart_category")
public class MartCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mart_category_id")
    private Long martCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "martshop_id")
    private MartShop martShop;

    @Column(name = "category_name", length = 255)
    private String categoryName;

    @Column(name = "category_index")
    private Integer categoryIndex;
}
