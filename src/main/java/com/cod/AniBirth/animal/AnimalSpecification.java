package com.cod.AniBirth.animal;

import com.cod.AniBirth.animal.entity.Animal;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class AnimalSpecification {
    public static Specification<Animal> searchWith(AnimalSearchDTO searchDTO) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (searchDTO.getKeyword() != null && !searchDTO.getKeyword().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("name"), "%" + searchDTO.getKeyword() + "%"));
            }
            if (searchDTO.getClassification() != null) {
                predicates.add(criteriaBuilder.equal(root.get("classification").get("id"), searchDTO.getClassification()));
            }
            if (searchDTO.getGender() != null) {
                predicates.add(criteriaBuilder.equal(root.get("gender").get("id"), searchDTO.getGender()));
            }
            if (searchDTO.getWeight() != null) {
                predicates.add(criteriaBuilder.equal(root.get("weight").get("id"), searchDTO.getWeight()));
            }
            if (searchDTO.getAge() != null) {
                predicates.add(criteriaBuilder.equal(root.get("age").get("id"), searchDTO.getAge()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
