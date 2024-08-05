package com.cod.AniBirth.article.repository;

import com.cod.AniBirth.article.entity.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Override
    Page<Article> findAll(Pageable pageable);

    Article findFirstByIdLessThanOrderByIdDesc(Long id);
    Article findFirstByIdGreaterThanOrderByIdAsc(Long id);
}
