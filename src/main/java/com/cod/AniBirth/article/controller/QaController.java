package com.cod.AniBirth.article.controller;

import com.cod.AniBirth.article.entity.Qa;
import com.cod.AniBirth.article.service.QaService;
import com.cod.AniBirth.member.entity.Member;
import com.cod.AniBirth.member.service.MemberService;
import com.cod.AniBirth.global.security.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/qa")
public class QaController {

    private final QaService qaService;
    private final MemberService memberService;

    @GetMapping("/list")
    public String list(Model model,
                       @RequestParam(value = "page", defaultValue = "0") int page,
                       @RequestParam(value = "size", defaultValue = "10") int size,
                       Authentication authentication) {

        Member member = null;
        if (authentication != null && authentication.isAuthenticated()) {
            member = memberService.findByUsername(authentication.getName());
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createDate"));
        Page<Qa> paging = qaService.getList(pageable);
        model.addAttribute("paging", paging);
        model.addAttribute("member", member);

        return "qa/list";
    }

    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("qa", new Qa());
        return "qa/form";
    }

    @PostMapping("/create")
    public String create(@ModelAttribute Qa qa, Authentication authentication) {
        String username = null;
        if (authentication.getPrincipal() instanceof UserDetails) {
            username = ((UserDetails) authentication.getPrincipal()).getUsername();
        } else if (authentication.getPrincipal() instanceof String) {
            username = (String) authentication.getPrincipal();
        }

        if (username == null) {
            throw new IllegalStateException("User is not authenticated");
        }

        Member member = memberService.findByUsername(username);
        qa.setMember(member);
        qa.setCreateDate(LocalDateTime.now());
        qaService.saveQa(qa);
        return "redirect:/qa/list";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model, Authentication authentication) {
        qaService.increaseViewCount(id);
        Qa qa = qaService.getQaById(id);
        if (qa == null) {
            throw new DataNotFoundException("해당 QA를 찾을 수 없습니다.");
        }

        Member member = null;
        if (authentication != null && authentication.isAuthenticated()) {
            member = memberService.findByUsername(authentication.getName());
        }

        model.addAttribute("qa", qa);
        model.addAttribute("member", member);

        return "qa/detail";
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Qa qa = qaService.getQaById(id);
        if (qa == null) {
            throw new DataNotFoundException("해당 QA를 찾을 수 없습니다.");
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        if (!qa.getMember().getUsername().equals(username)) {
            throw new SecurityException("게시물 수정 권한이 없습니다.");
        }

        model.addAttribute("qa", qa);
        return "qa/form";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, @ModelAttribute Qa qa, Authentication authentication) {
        Qa existingQa = qaService.getQaById(id);
        if (existingQa == null) {
            throw new DataNotFoundException("해당 QA를 찾을 수 없습니다.");
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        if (!existingQa.getMember().getUsername().equals(username)) {
            throw new SecurityException("게시물 수정 권한이 없습니다.");
        }

        existingQa.setTitle(qa.getTitle());
        existingQa.setContent(qa.getContent());
//        existingQa.setModifyDate(LocalDateTime.now());
        qaService.saveQa(existingQa);

        return "redirect:/qa/" + id;
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, Authentication authentication) {
        Qa qa = qaService.getQaById(id);
        if (qa == null) {
            throw new DataNotFoundException("해당 QA를 찾을 수 없습니다.");
        }

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        if (!qa.getMember().getUsername().equals(username)) {
            throw new SecurityException("게시물 삭제 권한이 없습니다.");
        }

        qaService.deleteQa(id);
        return "redirect:/qa/list";
    }
}
