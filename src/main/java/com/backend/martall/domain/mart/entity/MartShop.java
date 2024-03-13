package com.backend.martall.domain.mart.entity;

import com.backend.martall.domain.BaseTime;
import com.backend.martall.domain.mart.dto.MartRequestDto;
import com.backend.martall.domain.user.entity.User;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "mart_shop")
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    @OneToMany(mappedBy = "martShop", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MartShopPic> martShopPics;

    @OneToMany(mappedBy = "martShop", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MartCategory> martCategories;

    @OneToMany(mappedBy = "martShop", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<MartBookmark> martBookmarks;

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


    public void addMartCategory(MartCategory martCategory) {
        this.martCategories.add(martCategory);
        martCategory.setMartShop(this);
    }

    public void removeMartCategory(MartCategory martCategory) {
        this.martCategories.remove(martCategory);
    }

    public void addBookmark(MartBookmark martBookmark) {
        this.martBookmarks.add(martBookmark);
        martBookmark.setMartShop(this);
    }

    public void removeBookmark(MartBookmark martBookmark) {
        this.martBookmarks.remove(martBookmark);
    }

    public void addUser(User user) {
        this.user = user;
    }

    public void updateMartShop(MartRequestDto martRequestDto) {
        if (martRequestDto.getName() != null) this.name = martRequestDto.getName();
        if (martRequestDto.getIntroduction() != null) this.introduction = martRequestDto.getIntroduction();
        if (martRequestDto.getAddress() != null) this.address = martRequestDto.getAddress();
        if (martRequestDto.getOperatingTime() != null) this.operatingTime = martRequestDto.getOperatingTime();
        if (martRequestDto.getPickupTime() != null) this.pickupTime = martRequestDto.getPickupTime();
        if (martRequestDto.getVisitor() != null) this.visitor = martRequestDto.getVisitor();
        if (martRequestDto.getSale() != null) this.sale = martRequestDto.getSale();
        if (martRequestDto.getProfilePhoto() != null) this.profilePhoto = martRequestDto.getProfilePhoto();
        if (martRequestDto.getManagerName() != null) this.managerName = martRequestDto.getManagerName();
        if (martRequestDto.getShopNumber() != null) this.shopNumber = martRequestDto.getShopNumber();
        if (martRequestDto.getLinkKakao() != null) this.linkKakao = martRequestDto.getLinkKakao();
        if (martRequestDto.getLinkNaver() != null) this.linkNaver = martRequestDto.getLinkNaver();
        if (martRequestDto.getLongitude() != null) this.longitude = martRequestDto.getLongitude();
        if (martRequestDto.getLatitude() != null) this.latitude = martRequestDto.getLatitude();
    }
}
