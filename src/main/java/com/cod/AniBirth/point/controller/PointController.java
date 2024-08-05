package com.cod.AniBirth.point.controller;

import com.cod.AniBirth.account.entity.Account;
import com.cod.AniBirth.member.entity.Member;
import com.cod.AniBirth.member.service.MemberService;
import com.cod.AniBirth.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/points")
@RequiredArgsConstructor
public class PointController {
    private final PointService pointService;
    private final MemberService memberService;



    @GetMapping("/recharge")
    @PreAuthorize("isAuthenticated()")
    public String showRechargePage() {
        return "points/recharge";
    }


    @GetMapping("/success")
    @PreAuthorize("isAuthenticated()")
    public String rechargeSuccess(@RequestParam("paymentKey") String paymentKey,
                                  @RequestParam("amount") int amount,
                                  @RequestParam("orderId") String orderId,
                                  Model model) {
        Member member = memberService.getCurrentMember();
        String transactionId = paymentKey + "-" + orderId; // Generate a unique transaction ID
        pointService.rechargePoints(member, amount, transactionId);
        model.addAttribute("message", "포인트 충전이 완료되었습니다.");
        return "points/success";
    }

    @GetMapping("/fail")
    @PreAuthorize("isAuthenticated()")
    public String rechargeFail(@RequestParam("code") String code,
                               @RequestParam("message") String message,
                               Model model) {
        model.addAttribute("message", message);
        return "points/fail";
    }
    @GetMapping("/balance")
    @PreAuthorize("isAuthenticated()")
    @ResponseBody
    public Map<String, Long> getBalance() {
        Member member = memberService.getCurrentMember();
        Account account = pointService.getOrCreateAccount(member);
        Map<String, Long> response = new HashMap<>();
        response.put("balance", account.getAniPoint());
        return response;
    }


}