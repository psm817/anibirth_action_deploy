package com.cod.AniBirth;

import com.cod.AniBirth.article.service.ArticleService;
import com.cod.AniBirth.article.service.QaService;
import com.cod.AniBirth.member.service.MemberService;
import com.cod.AniBirth.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ArticleServiceTests {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private QaService qaService;

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("테스트 공지사항 생성")
    void testCreateArticles() {
//        String username = "user";

        for (int i = 1; i <= 20; i++) {
            String title = String.format("테스트 공지사항:[%03d]", i);
            String content = String.format("테스트 내용 %03d", i);
            articleService.create(title, content);
        }
    }
    @Test
    @DisplayName("테스트 Q&A 생성")
    void testCreateQas() {
        String username = "user1";

        for (int i = 1; i <= 20; i++) {
            String title = String.format("테스트 Q&A:[%03d]", i);
            String content = String.format("테스트 내용 %03d", i);
            qaService.create(title, content, username);
        }
    }
    @Test
    @DisplayName("강의 제품 생성")
    void test1(){
        for (int i=1; i<=20; i++){
            String name =String.format("테스트 상품:[%03d]",i);
            String description = String.format("테스트 설명:[%03d]",i);
            int price = 1;
            productService.create(name, description ,price);

        }
    }
}
