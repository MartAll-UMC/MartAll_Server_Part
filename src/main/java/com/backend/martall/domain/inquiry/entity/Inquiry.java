package com.backend.martall.domain.inquiry.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "INQUIRY")
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "INQUIRY_ID")
    private Long inquiryId;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CONTENT")
    private String content;

    @Column(name = "REG_TIME", updatable = false)
    private LocalDateTime regTime;

    @Column(name = "INQUIRY_STATE")
    private String inquiryState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    private User user;

    @PrePersist
    public void onPrePersist() {
        this.regTime = LocalDateTime.now();
    }
}
