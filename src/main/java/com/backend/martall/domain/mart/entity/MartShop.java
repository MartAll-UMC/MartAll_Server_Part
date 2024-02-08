package com.backend.martall.domain.mart.entity;

import com.backend.martall.domain.BaseTime;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Table(name = "mart_shop")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MartShop extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "martshop_id", nullable = false)
    private Long martShopId;

    @Column(name = "name", length = 50)
    private String name;

    @Column(name = "introduction", length = 255)
    private String introduction;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "reg_datetime")
    private LocalDateTime registrationDateTime;

    @Column(name = "operating_time", length = 50)
    private String operatingTime;

    @Column(name = "pickup_time", length = 50)
    private String pickupTime;

    @Column(name = "visitor")
    private Integer visitor;

    @Column(name = "sale")
    private Integer sale;

    @Column(name = "profile_photo", length = 255)
    private String profilePhoto;

    @Column(name = "user_idx")
    private Long userIdx;

    @Column(name = "mart_category_id")
    private Long martCategoryId;

    @Column(name = "manager_name", length = 50)
    private String managerName;

    @Column(name = "shop_number", length = 50)
    private String shopNumber;

    @Column(name = "link_kakao", length = 255)
    private String linkKakao;

    @Column(name = "link_naver", length = 255)
    private String linkNaver;

    @Column(name = "longitude", length = 50)
    private String longitude;

    @Column(name = "latitude", length = 50)
    private String latitude;


    /*@OneToMany(mappedBy = "martShop", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MartShopPic> martShopPics; */
    /*@OneToMany(mappedBy = "martShop", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MartCategory> martCategories; */
}
