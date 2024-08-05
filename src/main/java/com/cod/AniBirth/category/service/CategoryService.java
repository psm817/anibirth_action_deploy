package com.cod.AniBirth.category.service;

import com.cod.AniBirth.account.entity.Account;
import com.cod.AniBirth.animal.entity.Animal;
import com.cod.AniBirth.category.entity.Category;
import com.cod.AniBirth.category.repository.CategoryRepository;
import com.cod.AniBirth.member.entity.Member;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getClassificationCategories() {
        Category classification = categoryRepository.findByName("동물구분");
        return categoryRepository.findByParent(classification);
    }
    public List<Category> getAgeCategories() {
        Category age = categoryRepository.findByName("연령");
        return categoryRepository.findByParent(age);
    }

    public List<Category> getWeightCategories() {
        Category weight = categoryRepository.findByName("크기");
        return categoryRepository.findByParent(weight);
    }

    public List<Category> getGenderCategories() {
        Category gender = categoryRepository.findByName("성별");
        return categoryRepository.findByParent(gender);
    }

    @PostConstruct
    public void initCategories() {
        // 상위 카테고리 생성
        Category classification = Category.builder()
                .name("동물구분")
                .build();
        categoryRepository.save(classification);

        Category gender = Category.builder()
                .name("성별")
                .build();
        categoryRepository.save(gender);

        Category weight = Category.builder()
                .name("크기")
                .build();
        categoryRepository.save(weight);

        Category age = Category.builder()
                .name("연령")
                .build();
        categoryRepository.save(age);

        // 하위 카테고리 생성 및 상위 카테고리와 연결
        Category dog = Category.builder()
                .name("개")
                .parent(classification)
                .build();
        Category cat = Category.builder()
                .name("고양이")
                .parent(classification)
                .build();
        Category other = Category.builder()
                .name("기타동물")
                .parent(classification)
                .build();
        categoryRepository.saveAll(Arrays.asList(dog, cat, other));

        Category male = Category.builder()
                .name("수")
                .parent(gender)
                .build();
        Category female = Category.builder()
                .name("암")
                .parent(gender)
                .build();
        categoryRepository.saveAll(Arrays.asList(male, female));

        Category small = Category.builder()
                .name("소(8kg 미만)")
                .parent(weight)
                .build();
        Category middle = Category.builder()
                .name("중(8~18kg 미만)")
                .parent(weight)
                .build();
        Category big = Category.builder()
                .name("대(18kg 이상)")
                .parent(weight)
                .build();
        categoryRepository.saveAll(Arrays.asList(small, middle,big));

        Category Puppy = Category.builder()
                .name("Puppy(~ 6개월)")
                .parent(age)
                .build();
        Category Junior = Category.builder()
                .name("Junior(7개월 ~ 2살)")
                .parent(age)
                .build();
        Category Adult = Category.builder()
                .name("Adult(3살 ~ 8살)")
                .parent(age)
                .build();
        Category Senior = Category.builder()
                .name("Senior(9살 ~)")
                .parent(age)
                .build();
        categoryRepository.saveAll(Arrays.asList(Puppy, Junior, Adult, Senior));
    }

    public List<Category> getClassifications() {
        return categoryRepository.findByParentName("동물구분");
    }

    public List<Category> getGenders() {
        return categoryRepository.findByParentName("성별");
    }

    public List<Category> getWeights() {
        return categoryRepository.findByParentName("크기");
    }

    public List<Category> getAges() {
        return categoryRepository.findByParentName("연령");
    }
}
