package com.backend.martall.domain.mart.dto;

import com.backend.martall.domain.mart.entity.MartShop;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor


public class MartResponseDto {
    private Long martShopId;
    private String name;
    private String introduction;
    private String address;
    private LocalDateTime registrationDateTime;
    private String operatingTime;
    private String pickupTime;
    private Integer visitor;
    private Integer sale;
    private String profilePhoto;
    private Long userIdx;
    private Long martCategoryId;
    private String managerName;
    private String shopNumber;
    private String linkKakao;
    private String linkNaver;
    private String longitude;
    private String latitude;

    public static MartResponseDto from(MartShop martShop) {
        MartResponseDto dto = new MartResponseDto();
        dto.martShopId = martShop.getMartShopId();
        dto.name = martShop.getName();
        dto.introduction = martShop.getIntroduction();
        dto.address = martShop.getAddress();
        dto.registrationDateTime = martShop.getRegistrationDateTime();
        dto.operatingTime = martShop.getOperatingTime();
        dto.pickupTime = martShop.getPickupTime();
        dto.visitor = martShop.getVisitor();
        dto.sale = martShop.getSale();
        dto.profilePhoto = martShop.getProfilePhoto();
        dto.userIdx = martShop.getUserIdx();
        dto.martCategoryId = martShop.getMartCategoryId();
        dto.managerName = martShop.getManagerName();
        dto.shopNumber = martShop.getShopNumber();
        dto.linkKakao = martShop.getLinkKakao();
        dto.linkNaver = martShop.getLinkNaver();
        dto.longitude = martShop.getLongitude();
        dto.latitude = martShop.getLatitude();
        return dto;
    }


}
