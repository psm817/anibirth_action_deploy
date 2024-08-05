package com.cod.AniBirth.product.controller;


import com.cod.AniBirth.member.entity.Member;
import com.cod.AniBirth.member.service.MemberService;
import com.cod.AniBirth.product.entity.Product;
import com.cod.AniBirth.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final MemberService memberService;

    @GetMapping("/main")
    public String product() {
        return "product/main";
    }


    @GetMapping("/list")
    public String list(
            Model model,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value="kw", defaultValue="") String kw,
            Authentication authentication
    ) {
        Page<Product> paging = productService.getList(page, kw);


        Member member = null;
        if (authentication != null && authentication.isAuthenticated()) {
            member = memberService.findByUsername(authentication.getName());
        }

        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        model.addAttribute("member", member); // 모델에 isAdmin 속성 추가

        return "product/list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Product product = productService.getProduct(id);

        Member member = null;
        if (authentication != null && authentication.isAuthenticated()) {
            member = memberService.findByUsername(authentication.getName());
        }

        model.addAttribute("product", product);
        model.addAttribute("member", member);

        return "product/detail";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(Model model, Authentication authentication) {
        Member member = memberService.findByUsername(authentication.getName());
        model.addAttribute("member", member);
        return "product/create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String createContent(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("price") int price,
            @RequestParam("thumbnail") MultipartFile thumbnail,
            Authentication authentication
    ) {
        Member member = memberService.findByUsername(authentication.getName());
        productService.create(title, description, price, thumbnail, member);

        return "redirect:/product/list";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProduct(id);
        model.addAttribute("product", product);
        return "product/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modifyContent(
            @PathVariable("id") Long id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("price") int price,
            @RequestParam(value = "thumbnail", required = false) MultipartFile thumbnail
    ) {
        productService.modify(id, title, description, price, thumbnail);
        return "redirect:/product/detail/" + id;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        productService.delete(id);
        return "redirect:/product/list";
    }

}
