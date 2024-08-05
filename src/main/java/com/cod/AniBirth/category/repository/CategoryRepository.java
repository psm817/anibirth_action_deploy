package com.cod.AniBirth.category.repository;

import com.cod.AniBirth.animal.entity.Animal;
import com.cod.AniBirth.category.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository <Category, Long> {
//    List<Animal> findByCategory(Category category);
    Category findByName(String name);
    // 카테고리 타입으로 조회
    List<Category> findByType(String type);
    List<Category> findByParent(Category parent);
    List<Category> findByParentName(String parentName);
}
