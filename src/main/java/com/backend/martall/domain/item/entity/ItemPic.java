package com.backend.martall.domain.item.entity;

import com.backend.martall.domain.BaseTime;
import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "mart_item_pic")
public class ItemPic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_pic_id")
    private int itemPicId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Item picItem;

    @Column(name = "pic_name")
    private String picName;

    @Column(name = "pic_index")
    private int picIndex;
}
