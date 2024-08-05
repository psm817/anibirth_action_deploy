package com.cod.AniBirth.order.entity;

import com.cod.AniBirth.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class OrderItem extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Order order;
    private LocalDateTime payDate;
//    @ManyToOne(fetch = LAZY)
//    private Product product;

    private int price; // 판매가
    private int payPrice; // 결제금액
    private boolean isPaid; // 결제여부
}
