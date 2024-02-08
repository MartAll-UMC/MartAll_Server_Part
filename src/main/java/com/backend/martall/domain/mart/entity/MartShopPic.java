package com.backend.martall.domain.mart.entity;

import com.backend.martall.domain.BaseTime;
import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "martshoppic")
public class MartShopPic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long martShopPicId;

    @Column(name = "pic_name")
    private String picName;

    @Column(name = "index")
    private Integer picindex;
}