package com.cod.AniBirth.review.entity;


import com.cod.AniBirth.base.entity.BaseEntity;
import com.cod.AniBirth.member.entity.Member;
import com.cod.AniBirth.product.entity.Product;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Review extends BaseEntity {
    private String content;
    private int starRating;

    @ManyToOne
    private Member member;
    @ManyToOne
    private Product product;

}
