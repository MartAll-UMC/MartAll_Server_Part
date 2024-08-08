package com.backend.martall.domain.inquiry.entity;

import com.backend.martall.domain.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InquiryContent extends BaseTime {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private Boolean isUser;

    @ManyToOne
    @JoinColumn(name = "inquiry_id")
    private Inquiry inquiry;
}
