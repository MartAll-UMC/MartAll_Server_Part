package com.backend.martall.domain.user.entity;

import com.backend.martall.domain.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.ColumnDefault;

@Table(name = "user")
@Entity
@Getter
@NoArgsConstructor
public class User extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_idx")
    private Long userIdx;
    private String id;
    private String password;
    private String username;
    private String phoneNumber;
    @Column(name = "img_url")
    private String imgUrl;
    private String email;
    private String provider;
    @Column(name = "provider_id")
    private Long providerId;
    @Column(name = "user_type")
    private Integer userType;
    @Column(name = "user_state")
    private Integer userState;
    @ColumnDefault("0")
    private Integer money = 0;
    @Column(name = "fcm_token")
    private String fcmToken;
    //@Column(columnDefinition = "double default 0.0")
    private Double longitude;
    private Double latitude;
    private String address;
    //@Column(name = "location_range", nullable = false, columnDefinition = "int default 0")
    private Integer locationRange = 0;

    @Builder
    public User(String id, String password, String username, String imgUrl, String phoneNumber, String email, String provider,
                Long providerId, Integer userType, Integer userState, String fcmToken) {
        this.id = id;
        this.password = password;
        this.username = username;
        this.imgUrl = imgUrl;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.provider = provider;
        this.providerId = providerId;
        this.userType = userType;
        this.userState = userState;
        this.fcmToken = fcmToken;
    }

    public Long getUserIdx() {
        return userIdx;
    }
    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getImgUrl() { return imgUrl; }
    public String getEmail() { return email; }
    public String getProvider() { return provider; }
    public Integer getMoney() { return money; }
    public Double getLongitude() { return longitude; }
    public Double getLatitude() { return latitude; }
    public String getAddress() { return address; }
    public Integer getLocationRange() { return locationRange; }
}
