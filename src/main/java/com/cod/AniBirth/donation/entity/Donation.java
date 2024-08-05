package com.cod.AniBirth.donation.entity;

import com.cod.AniBirth.base.entity.BaseEntity;
import com.cod.AniBirth.member.entity.Member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;

@Entity
@Setter
@Getter
public class Donation extends BaseEntity {


    @Column(nullable = false)
    private Long amount;

    @ManyToOne
    @JoinColumn(name = "donor_id", nullable = false)
    private Member donor; // 후원 보내는 사람

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false)
    private Member recipient; // 후원 받는 센터



}
