package com.backend.martall.domain.mart.dto;

import com.backend.martall.domain.mart.entity.MartShop;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MartCreateRequestDto {
    private String name;
    private String introduction;
    private String address;
    private String operatingTime;
    private String pickupTime;
    private Integer visitor;
    private Integer sale;
    private String profilePhoto;
    private String managerName;
    private String shopNumber;
    private String linkKakao;
    private String linkNaver;
    private String longitude;
    private String latitude;

   //MartShop 엔티티 인스턴스 생성, 초기화
    public MartShop toEntity() {
        return MartShop.builder()
                .name(this.name)
                .introduction(this.introduction)
                .address(this.address)
                .operatingTime(this.operatingTime)
                .pickupTime(this.pickupTime)
                .visitor(this.visitor)
                .sale(this.sale)
                .profilePhoto(this.profilePhoto)
                .managerName(this.managerName)
                .shopNumber(this.shopNumber)
                .linkKakao(this.linkKakao)
                .linkNaver(this.linkNaver)
                .longitude(this.longitude)
                .latitude(this.latitude)
                .build();
    }

}


