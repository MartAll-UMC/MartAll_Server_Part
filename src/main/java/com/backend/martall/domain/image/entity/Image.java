package com.backend.martall.domain.image.entity;

import com.backend.martall.domain.BaseTime;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@Table(name = "image")
@NoArgsConstructor
@Entity
public class Image extends BaseTime {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "img_idx")
    private Long imgIdx;

    private String imgUrl;

    private String category;

    @Column(name = "category_idx")
    private Long categoryIdx;

    @Builder
    public Image(String imgUrl, String category, Long category_idx){
        this.imgUrl = imgUrl;
        this.category = category;
        this.categoryIdx = category_idx;
    }
}
