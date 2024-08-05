package com.cod.AniBirth.review.controller;

import com.cod.AniBirth.member.entity.Member;
import com.cod.AniBirth.member.service.MemberService;
import com.cod.AniBirth.product.entity.Product;
import com.cod.AniBirth.product.service.ProductService;
import com.cod.AniBirth.review.entity.Review;
import com.cod.AniBirth.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/review")
public class ReviewController {
    private final ReviewService reviewService;
    private final ProductService productService;
    private final MemberService memberService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create/{id}")
    public String create(
            @PathVariable("id") Long id,
            Principal principal,
            @RequestParam("content") String content,
            @RequestParam("starRating") int starRating
    ) {
        Product product = productService.getProduct(id);
        Member member = memberService.findByUsername(principal.getName());

        reviewService.create(product, member, content, starRating);

        return String.format("redirect:/product/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(
            @PathVariable("id") Long id,
            Principal principal,
            @RequestParam("content") String content,
            @RequestParam("starRating") int starRating
    ) {
        Review review = reviewService.getReview(id);
        reviewService.modify(review, content, starRating);
        long productId = review.getProduct().getId();

        if (!review.getMember().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한 없음");
        }

        return String.format("redirect:/product/detail/%s", productId);
    }
}
