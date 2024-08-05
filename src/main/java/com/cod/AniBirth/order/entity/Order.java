package com.cod.AniBirth.order.entity;


import com.cod.AniBirth.base.entity.BaseEntity;
import com.cod.AniBirth.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table(name="product_order")
public class Order extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    private Member buyer;
    private String name;
    private boolean isPaid;
    private boolean isCanceled;
    private boolean isRefunded;

    @OneToMany(mappedBy = "order", cascade = CascadeType.REMOVE)
    private List<OrderItem> orderItemList;
}