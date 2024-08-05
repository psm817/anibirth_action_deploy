package com.cod.AniBirth.category.entity;

import com.cod.AniBirth.animal.entity.Animal;
import com.cod.AniBirth.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseEntity {

    private String name;

    @Column
    private String type; // 카테고리의 타입 (예: gender, age, classification, weight)

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children;

    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private List<Animal> animals = new ArrayList<>();
    @Override
    public String toString() {
        return "Category [name=" + name + ", type=" + type + "]";
    }
}
