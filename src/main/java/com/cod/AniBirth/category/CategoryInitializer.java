package com.cod.AniBirth.category;

import com.cod.AniBirth.category.entity.Category;
import com.cod.AniBirth.category.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CategoryInitializer {
    @Autowired
    private CategoryRepository categoryRepository;

    public void initializeCategories() {
        // 예시로 카테고리를 생성합니다.
        createCategoryIfNotExists("Male", "gender");
        createCategoryIfNotExists("Female", "gender");

        createCategoryIfNotExists("Puppy", "age");
        createCategoryIfNotExists("Adult", "age");

        createCategoryIfNotExists("Dog", "classification");
        createCategoryIfNotExists("Cat", "classification");
        createCategoryIfNotExists("Other", "classification");

        createCategoryIfNotExists("Small", "weight");
        createCategoryIfNotExists("Medium", "weight");
        createCategoryIfNotExists("Large", "weight");
    }
    private void createCategoryIfNotExists(String name, String type) {
        if (categoryRepository.findByName(name) == null) {
            Category category = Category.builder()
                    .name(name)
                    .type(type)
                    .build();
            categoryRepository.save(category);
        }
    }
}
