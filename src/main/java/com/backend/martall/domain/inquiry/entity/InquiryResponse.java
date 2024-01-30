package com.backend.martall.domain.inquiry.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "INQUIRY_RESPONSE")
public class InquiryResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INQUIRY_RESPONSE_ID")
    private Long inquiryResponseId;

    @Column(name = "REG_TIME", updatable = false)
    private LocalDateTime regTime;

    @Column(name = "CONTENT")
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INQUIRY_ID")
    private Inquiry inquiry;



    @PrePersist
    public void onPrePersist() {
        this.regTime = LocalDateTime.now();
    }
}
