package com.cod.AniBirth.order.controller;

import com.cod.AniBirth.account.entity.Account;
import com.cod.AniBirth.member.entity.Member;
import com.cod.AniBirth.member.service.MemberService;
import com.cod.AniBirth.order.service.OrderService;
import com.cod.AniBirth.point.service.PointService;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    @Value("${custom.paymentSecretKey}")
    private String paymentSecretKey;

    private final OrderService orderService;
    private final PointService pointService;
    private final MemberService memberService;

    @GetMapping("/success")
    public String paymentResult(
            Model model,
            @RequestParam(value = "orderId") String orderId,
            @RequestParam(value = "amount") Integer amount,
            @RequestParam(value = "paymentKey") String paymentKey,
            @RequestParam(value = "usePoints", required = false) Boolean usePoints) throws Exception {

        Member member = memberService.getCurrentMember();

        if (usePoints != null && usePoints) {
            Account account = pointService.getOrCreateAccount(member);
            if (account.getAniPoint() >= amount) {
                account.setAniPoint(account.getAniPoint() - amount);
                pointService.save(account);
                model.addAttribute("isSuccess", true);
                model.addAttribute("responseStr", "Payment completed using points");
                return "order/success";
            } else {
                model.addAttribute("isSuccess", false);
                model.addAttribute("responseStr", "Insufficient points");
                return "points/fail";
            }
        }

        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode(paymentSecretKey.getBytes("UTF-8"));
        String authorization = "Basic " + new String(encodedBytes, 0, encodedBytes.length);

        URL url = new URL("https://api.tosspayments.com/v1/payments/" + paymentKey);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorization);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);
        obj.put("amount", amount);

        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200;
        model.addAttribute("isSuccess", isSuccess);

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = (JSONObject) parser.parse(reader);
        responseStream.close();
        model.addAttribute("responseStr", jsonObject.toJSONString());
        System.out.println(jsonObject.toJSONString());

        model.addAttribute("method", (String) jsonObject.get("method"));
        model.addAttribute("orderName", (String) jsonObject.get("orderName"));

        if (jsonObject.get("method") != null) {
            switch ((String) jsonObject.get("method")) {
                case "카드":
                    model.addAttribute("cardNumber", (String) ((JSONObject) jsonObject.get("card")).get("number"));
                    break;
                case "가상계좌":
                    model.addAttribute("accountNumber", (String) ((JSONObject) jsonObject.get("virtualAccount")).get("accountNumber"));
                    break;
                case "계좌이체":
                    model.addAttribute("bank", (String) ((JSONObject) jsonObject.get("transfer")).get("bank"));
                    break;
                case "휴대폰":
                    model.addAttribute("customerMobilePhone", (String) ((JSONObject) jsonObject.get("mobilePhone")).get("customerMobilePhone"));
                    break;
                default:
                    model.addAttribute("code", (String) jsonObject.get("code"));
                    model.addAttribute("message", (String) jsonObject.get("message"));
                    break;
            }
        } else {
            model.addAttribute("code", (String) jsonObject.get("code"));
            model.addAttribute("message", (String) jsonObject.get("message"));
        }

        return "order/success";
    }
}
