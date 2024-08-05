package com.cod.AniBirth.cart.entity;


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

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem extends BaseEntity {
    @ManyToOne
    private Member member;

    @ManyToOne
    private Product product;
}
