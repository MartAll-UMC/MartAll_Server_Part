package com.backend.martall.domain.mart.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MartShopPic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pic_name")
    private String picName;

    @Column(name = "pic_index")
    private Integer picIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "martshop_id")
    private MartShop martShop;

}
