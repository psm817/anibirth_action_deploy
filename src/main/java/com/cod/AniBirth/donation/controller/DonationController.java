package com.cod.AniBirth.donation.controller;

import com.cod.AniBirth.donation.entity.Donation;
import com.cod.AniBirth.donation.service.DonationService;
import com.cod.AniBirth.member.entity.Member;
import com.cod.AniBirth.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/donation")
@RequiredArgsConstructor
@Slf4j
public class DonationController {

    private final DonationService donationService;
    private final MemberService memberService;

    @GetMapping("/donate")
    public String showDonatePage(Model model, Authentication authentication) {
        List<Member> recipients = donationService.getAllRecipients();
        Member member = null;

        if (authentication != null && authentication.isAuthenticated()) {
            member = memberService.findByUsername(authentication.getName());
        }

        // 모델에 상위 기부자를 추가합니다
        List<Object[]> topDonors = donationService.getTopDonors();
        model.addAttribute("topDonors", topDonors != null ? topDonors : new ArrayList<>());
        model.addAttribute("recipients", recipients != null ? recipients : new ArrayList<>());
        model.addAttribute("isAuthenticated", authentication != null && authentication.isAuthenticated());
        model.addAttribute("member", member);
        return "donation/donation";
    }

    @PostMapping("/submit")
    public String donate(
            @RequestParam("recipientId") Long recipientId,
            @RequestParam(value = "amount", required = false) Long amount,
            Model model) {

        if (amount == null || amount <= 0) {
            model.addAttribute("message", "금액을 입력하세요.");
            return "redirect:/donation/fail";
        }

        // 현재 로그인된 회원 정보를 가져옵니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member donor = memberService.findByUsername(authentication.getName());
        Member recipient = memberService.getMemberById(recipientId);

        if (donor == null || recipient == null) {
            model.addAttribute("message", "후원자가 유효하지 않습니다.");
            return "redirect:/donation/fail";
        }

        boolean success = donationService.donatePoints(donor, recipient, amount);

        if (success) {
            model.addAttribute("message", "후원이 성공적으로 완료되었습니다.");
            return "donation/success";
        } else {
            model.addAttribute("message", "포인트가 부족합니다.");
            return "donation/fail";
        }
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParams(MissingServletRequestParameterException ex, Model model) {
        model.addAttribute("message", "모든 필드를 입력하세요.");
        return "donation/fail";
    }

    @GetMapping("/fail")
    public String donationFail(Model model) {
        model.addAttribute("message", "후원에 실패했습니다. 다시 시도해주세요.");
        return "donation/fail"; // Thymeleaf 템플릿의 이름 (fail.html)
    }

}
