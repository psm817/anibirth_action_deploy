package com.cod.AniBirth.animal;

import lombok.Data;


@Data
public class AnimalSearchDTO {
    private String keyword;
    private String classification;
    private String gender;
    private String weight;
    private String age;
}
