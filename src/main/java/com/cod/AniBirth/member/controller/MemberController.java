package com.cod.AniBirth.member.controller;

import com.cod.AniBirth.account.entity.Account;
import com.cod.AniBirth.account.service.AccountService;
import com.cod.AniBirth.email.service.EmailService;
import com.cod.AniBirth.member.entity.Member;
import com.cod.AniBirth.member.form.MemberForm;
import com.cod.AniBirth.member.repository.MemberRepository;
import com.cod.AniBirth.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.UUID;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final EmailService emailService;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/agreement")
    public String agreement() {
        return "member/agreement";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        return "member/signup"; // 회원가입 페이지로 이동
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/signup")
    public String signupMember(@Valid MemberForm memberForm, @RequestParam("thumbnailImg") MultipartFile thumbnailImg) {
        String imageFileName = storeProfilePicture(memberForm.getThumbnailImg());

        Member member = memberService.signup(memberForm.getUsername(), memberForm.getPassword(), memberForm.getNickname(),
                memberForm.getEmail(), memberForm.getPhone(), memberForm.getAddress(),
                imageFileName, memberForm.getAuthority(), memberForm.getIsActive());

        String subject = "애니버스 - 서비스 가입 환영";

        String body = String.format(
                "안녕하세요, <b>%s</b>님<br><br>"+
                        "애니버스에 회원가입해 주셔서 진심으로 감사드립니다. 저희는 유기동물 봉사, 입양, 후원을 통해 따뜻한 사회를 만들어 나가는 커뮤티니 사이트입니다.<br><br>"+
                        "이제 <b>%s</b>님도 저희와 함께 유기동물들에게 더 나은 미래를 선물할 수 있게 되었습니다.<br><br>"+
                        "애니버스에 가입하신 여러분께서는 다음과 같은 특별한 혜택을 제공받으실 수 있습니다.<br><br>"+
                        "1. 다양한 봉사 활동에 참여하여 유기동물들의 삶을 개선할 수 있습니다.<br>"+
                        "2. 사랑스러운 유기동물들에게 따뜻한 가정을 제공해 주세요!~<br>"+
                        "3. 유기동물 보호와 관리를 위해 후원을 할 수 있습니다!~<br>"+
                        "4. 다른 회원들과 소통하며 봉사, 입양, 후원에 대한 다양한 이야기를 나눠보세요!~<br><br>"+
                        "<b>%s</b>님의 참여와 관심이 유기동물들에게 큰 힘이 됩니다. 앞으로 저희 애니버스와 함께 따뜻한 손길을 나누며 더 나은 세상을 만들어 나가길 바랍니다.<br><br>"+
                        "언제든지 궁금한 점이나 도움이 필요하시면 애니버스에 연락 부탁드립니다. 항상 <b>%s</b>님의 의견에 귀 기울이며 더욱 발전해 나가겠습니다.<br><br>"+
                        "다시 한번 환영하며, 감사합니다.<br><br>"+
                        "따뜻한 하루 보내세요.<br><br>"+
                        "애니버스 팀 드림<br>"+
                        "고객지원 이메일 주소 : 5004jse@gmail.com<br>"+
                        "웹 사이트 주소 : http://localhost:8040",
                memberForm.getUsername(), memberForm.getUsername(), memberForm.getUsername(), memberForm.getUsername()
        );

        emailService.send(memberForm.getEmail(), subject, body);

        return "redirect:/member/login"; // 회원가입 후 로그인 페이지로 리다이렉트
    }

    public String storeProfilePicture(MultipartFile profilePicture) {
        // 이미지 저장 디렉토리 경로
        String uploadDir = "C:\\work\\AniBirth\\src\\main\\resources\\static\\images\\profile";

        // 디렉토리가 존재하지 않으면 생성
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (IOException e) {
                throw new IllegalStateException("Could not create upload directory", e);
            }
        }
        // 파일명 중복을 피하기 위해 임의의 파일명을 생성합니다.
        String fileName = UUID.randomUUID().toString(); // UUID로 파일명 생성
        String imageFileName = fileName + ".jpg"; // 파일 확장자 지정
        // 파일을 저장합니다.
        try {
            Path filePath = uploadPath.resolve(imageFileName);
            Files.copy(profilePicture.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Could not store image file", e);
        }
        // 저장된 파일의 상대 경로를 반환합니다.
        return "/images/profile/" + imageFileName;
    }

    @PostMapping("/sendPassword")
    public String sendPassword(@RequestParam(value="id", defaultValue = "") String id,
                               @RequestParam(value="email", defaultValue = "") String email, Model model) {
        String subject = "애니버스 - 임시비밀번호 발급";
        String code = emailService.createCode();
        String body = String.format(
                "안녕하세요, <b>%s</b>님<br><br>" +
                        "애니버스 사이트에 오신 것을 진심으로 환영합니다! 저희는 유기동물 봉사, 입양, 후원을 통해 따뜻한 사회를 만들어 나가는 커뮤티니 사이트입니다.<br><br>" +
                        "비밀번호를 잃어버리셔서 걱정 많으셨죠? 아래 임시비밀번호를 제공해드립니다.<br><br>" +
                        "임시비밀번호는 다음과 같습니다: <b>%s</b><br><br>" +
                        "제공된 임시비밀번호를 통해 로그인 부탁드리며, 개인정보 보호를 위해 마이페이지를 통해 반드시 비밀번호 변경을 요청드립니다.<br><br>" +
                        "더 궁금하신 점이나 도움이 필요하신 경우 언제든지 저희에게 연락해 주세요. 애니버스가 여러분께 많은 즐거움을 줄 수 있기를 바랍니다.<br><br>" +
                        "애니버스 팀 드림<br>"+
                        "고객지원 이메일 주소 : 5004jse@gmail.com<br>"+
                        "웹 사이트 주소 : http://localhost:8040",
                id, code
        );

        // username, email 둘다 만족하는 회원 찾기
        Member member = memberService.findByUsernameAndEmail(id, email);

        if(member != null && member.getUsername().equals(id) && member.getEmail().equals(email)) {
            // 임시비밀번호로 변경
            member.setPassword(passwordEncoder.encode(code));
            memberRepository.save(member);
            emailService.send(email, subject, body);

            return "redirect:/member/login?success=true";
        } else {
            model.addAttribute("error", "member not found");
            return "redirect:/member/login?error=true";
        }
    }

    @PostMapping("/sendId")
    public String sendId(@RequestParam(value="mail", defaultValue = "") String mail, Model model) {
        // email 만족하는 회원 찾기
        Member member = memberService.findByEmail(mail);

        if (member != null) {
            String subject = "애니버스 - 아이디 찾기";
            String body = String.format(
                    "애니버스 사이트에 오신 것을 진심으로 환영합니다! 저희는 유기동물 봉사, 입양, 후원을 통해 따뜻한 사회를 만들어 나가는 커뮤티니 사이트입니다.<br><br>" +
                            "아이디를 잃어버리셔서 걱정 많으셨죠? 아래 회원님의 아이디를 전달드립니다.<br><br>" +
                            "아이디는 다음과 같습니다: <b>%s</b><br><br>" +
                            "더 궁금하신 점이나 도움이 필요하신 경우 언제든지 저희에게 연락해 주세요. 애니버스가 여러분께 많은 즐거움을 줄 수 있기를 바랍니다.<br><br>" +
                            "애니버스 팀 드림<br>" +
                            "고객지원 이메일 주소 : 5004jse@gmail.com<br>" +
                            "웹 사이트 주소 : http://localhost:8040", member.getUsername()
            );

            if (member.getEmail().equals(mail)) {
                emailService.send(mail, subject, body);
                return "redirect:/member/login?success=true";
            }
        }

        // 이메일 주소가 일치하지 않거나 회원이 존재하지 않는 경우
        return "redirect:/member/login?incorrect=true";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, Model model) {
        Member member = memberService.getMemberById(id);
        model.addAttribute("member", member);

        // 소셜로그인은 비밀번호 변경 불가라 다른 폼으로 이동
        if(member.getUsername().startsWith("KAKAO") || member.getUsername().startsWith("NAVER") || member.getUsername().startsWith("GOOGLE")) {
            return "/member/social_modify";
        } else {
            return "/member/modify";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, @RequestParam("password") String password,
                         @RequestParam("nickname") String nickname, @RequestParam("email") String email,
                         @RequestParam("phone") String phone, @RequestParam("address") String address, @RequestParam("thumbnailImg") MultipartFile thumbnailImg) {
        Member member = memberService.getMemberById(id);
        Account account = accountService.findByMember(member);

        String imageFileName = null;
        if(thumbnailImg != null && !thumbnailImg.isEmpty()) {
            imageFileName = storeProfilePicture(thumbnailImg);
        }

        memberService.modify(member, password, nickname, email, phone, address, imageFileName);
        accountService.createOrUpdate(member, account.getAccount_number(), account.getAniPoint());

        return "redirect:/member/logout";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/socialModify/{id}")
    public String socialModify(@PathVariable("id") Long id, @RequestParam("nickname") String nickname, @RequestParam("email") String email,
                         @RequestParam("phone") String phone, @RequestParam("address") String address, @RequestParam("thumbnailImg") MultipartFile thumbnailImg) {
        Member member = memberService.getMemberById(id);
        Account account = accountService.findByMember(member);

        String imageFileName = null;
        if(thumbnailImg != null && !thumbnailImg.isEmpty()) {
            imageFileName = storeProfilePicture(thumbnailImg);
        }

        memberService.socialModify(member, nickname, email, phone, address, imageFileName);
        accountService.createOrUpdate(member, account.getAccount_number(), account.getAniPoint());

        return "redirect:/member/logout";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id, Principal principal) {
        Member member = memberService.getMemberById(id);

        if(!member.getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다.");
        }

        return "redirect:/member/logout";
    }
}
