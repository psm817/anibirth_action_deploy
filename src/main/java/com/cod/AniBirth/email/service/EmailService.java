package com.cod.AniBirth.email.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;

    public void send(String to, String subject, String body) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");

            mimeMessageHelper.setTo(to);                    // 메일 수신자
            mimeMessageHelper.setSubject(subject);          // 메일 제목
            mimeMessageHelper.setText(body, true);      // 메일 본문 내용, HTML 여부
            mailSender.send(mimeMessage);
        }
        catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public String createCode() {
        StringBuilder codeBuilder = new StringBuilder();
        Random random = new Random();
        String characters = "ABCDEFGHIJKLMNOPQRST" + UUID.randomUUID();
        int length = 8; // 인증번호 길이 (여기서는 8자리로 설정)

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            codeBuilder.append(characters.charAt(index));
        }

        return codeBuilder.toString();
    }
}
