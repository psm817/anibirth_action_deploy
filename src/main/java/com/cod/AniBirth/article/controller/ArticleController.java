package com.cod.AniBirth.article.controller;

import com.cod.AniBirth.article.entity.Article;
import com.cod.AniBirth.article.service.ArticleService;
import com.cod.AniBirth.global.security.DataNotFoundException;
import com.cod.AniBirth.member.entity.Member;
import com.cod.AniBirth.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/article")
public class ArticleController {
    private final ArticleService articleService;
    private final MemberService memberService;


    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "10") int size,
                       Authentication authentication) {

        Member member = null;

        if(authentication != null && authentication.isAuthenticated()) {
            member = memberService.findByUsername(authentication.getName());
        }

        Pageable pageable = PageRequest.of(page, size); // 페이지당 항목 수를 size로 설정
        Page<Article> paging = articleService.getList(pageable); // Pageable을 이용하여 페이지네이션을 수행
        model.addAttribute("paging", paging);
        model.addAttribute("member", member); // 모델에 isAdmin 속성 추가
        return "article/list";
    }


    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("article", new Article());
        return "article/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Article article) {
        article.setCreateDate(LocalDateTime.now());
        article.setViewCount(0);

        articleService.saveArticle(article);
        return "redirect:/article/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model) {
        Article article = articleService.getArticleById(id);
        if (article != null) {
            article.setViewCount(article.getViewCount() + 1);
            articleService.saveArticle(article);
        }
        Article prevArticle = articleService.getPreviousArticle(id);
        Article nextArticle = articleService.getNextArticle(id);

        model.addAttribute("article", article);
        model.addAttribute("prevArticleId", prevArticle != null ? prevArticle.getId() : null);
        model.addAttribute("nextArticleId", nextArticle != null ? nextArticle.getId() : null);
        return "article/detail";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model) {
        Article article = articleService.getArticleById(id);
        if (article == null) {
            throw new DataNotFoundException("Article not found");
        }
        model.addAttribute("article", article);
        return "article/form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, @ModelAttribute Article article) {
        Article existingArticle = articleService.getArticleById(id);
        if (existingArticle == null) {
            throw new DataNotFoundException("Article not found");
        }

        // 기존 Article 객체의 필드를 새로운 값으로 업데이트
        existingArticle.setTitle(article.getTitle());
        existingArticle.setContent(article.getContent());
        // 업데이트된 Article 저장
        articleService.saveArticle(existingArticle);

        return "redirect:/article/" + id;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        Article article = articleService.getArticleById(id);
        if (article == null) {
            throw new DataNotFoundException("Article not found");
        }
        articleService.deleteArticle(id);
        return "redirect:/article/list";
    }
}
