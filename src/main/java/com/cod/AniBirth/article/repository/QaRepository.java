package com.cod.AniBirth.article.repository;

import com.cod.AniBirth.article.entity.Article;
import com.cod.AniBirth.article.entity.Qa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QaRepository extends JpaRepository<Qa,Long> {

    @EntityGraph(attributePaths = "member")
    Page<Qa> findAll(Pageable pageable);

}
