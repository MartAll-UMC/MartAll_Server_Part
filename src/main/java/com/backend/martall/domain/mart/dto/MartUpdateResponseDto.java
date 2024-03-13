package com.backend.martall.domain.mart.dto;

import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.mart.repository.MartBookmarkRepository;
import com.backend.martall.domain.user.entity.User;
import com.backend.martall.domain.user.entity.UserRepository;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MartUpdateResponseDto {
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
    private Long martCategoryId;
    private String managerName;
    private String shopNumber;
    private String linkKakao;
    private String linkNaver;
    private String longitude;
    private String latitude;
    private boolean isFavorite;

    public static MartUpdateResponseDto from(MartShop martShop, Long userId, MartBookmarkRepository martBookmarkRepository, UserRepository userRepository) {
        MartUpdateResponseDto dto = new MartUpdateResponseDto();
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
        dto.managerName = martShop.getManagerName();
        dto.shopNumber = martShop.getShopNumber();
        dto.linkKakao = martShop.getLinkKakao();
        dto.linkNaver = martShop.getLinkNaver();
        dto.longitude = martShop.getLongitude();
        dto.latitude = martShop.getLatitude();

        // 단골 여부 설정
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        boolean isFavorite = martBookmarkRepository.existsByUserAndMartShop(user, martShop);


        return dto;
    }
}
