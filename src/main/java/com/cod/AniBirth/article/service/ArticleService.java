package com.cod.AniBirth.article.service;

import com.cod.AniBirth.article.entity.Article;
import com.cod.AniBirth.article.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;

    public List<Article> getList() {
        return articleRepository.findAll();
    }

    public void saveArticle(Article article) {
        if (article.getId() != null) {
            article.setModifyDate(LocalDateTime.now()); // 수정 시 updateDate 설정
        }
        articleRepository.save(article);
    }



    public Page<Article> getList(Pageable pageable) {
        Pageable sortedByCreateDateDesc = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createDate"));
        return articleRepository.findAll(sortedByCreateDateDesc);
    }


    public void create(String title, String content) {
        Article article = Article.builder()
                .title(title)
                .content(content)
                .createDate(LocalDateTime.now())
                .viewCount(0)
                .build();

        articleRepository.save(article);
    }
    public Article getArticleById(Long id) {
        Optional<Article> article = articleRepository.findById(id);
        return article.orElse(null);
    }

    public Article getPreviousArticle(Long id) {
        return articleRepository.findFirstByIdLessThanOrderByIdDesc(id);
    }

    public Article getNextArticle(Long id) {
        return articleRepository.findFirstByIdGreaterThanOrderByIdAsc(id);
    }

    public void deleteArticle(Long id) {
        // 삭제할 Article이 존재하는지 확인
        if (!articleRepository.existsById(id)) {
            throw new IllegalArgumentException("Article with ID " + id + " not found");
        }
        // Article 삭제
        articleRepository.deleteById(id);
    }
}
