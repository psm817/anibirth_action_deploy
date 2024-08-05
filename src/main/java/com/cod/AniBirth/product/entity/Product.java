package com.cod.AniBirth.product.entity;

import com.cod.AniBirth.base.entity.BaseEntity;
import com.cod.AniBirth.cart.entity.CartItem;
import com.cod.AniBirth.member.entity.Member;

import com.cod.AniBirth.review.entity.Review;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Product extends BaseEntity {
    private String title;
    private String shippingCost;
    private String description;
    private int price;
    private int hitCount;
    private String isActive;
    private String thumbnailImg;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<Review> reviewList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<CartItem> cartList;
}
