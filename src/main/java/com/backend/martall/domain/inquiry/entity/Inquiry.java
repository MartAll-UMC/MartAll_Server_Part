package com.backend.martall.domain.inquiry.entity;

import com.backend.martall.domain.BaseTime;
import com.backend.martall.domain.mart.entity.MartShop;
import com.backend.martall.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Inquiry extends BaseTime {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "martshop_id")
    private MartShop martShop;

    @OneToMany(mappedBy = "inquiry", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<InquiryContent> inquiryContentList = new ArrayList<>();

}
