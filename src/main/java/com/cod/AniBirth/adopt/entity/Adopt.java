package com.cod.AniBirth.adopt.entity;

import com.cod.AniBirth.base.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Adopt extends BaseEntity {

    @Column(length = 50) // VARCHAR(50)
    private String title;
    @Lob
    private String content;
    private int view;
    private String name;
    private String phone;

    /*@ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;*/

    /*@ManyToOne
    private User author;*/

}
