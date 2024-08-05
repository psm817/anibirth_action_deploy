package com.cod.AniBirth.volunteer.controller;

import com.cod.AniBirth.calendar.entity.Calendar;
import com.cod.AniBirth.calendar.service.CalendarService;
import com.cod.AniBirth.member.entity.Member;
import com.cod.AniBirth.member.service.MemberService;
import com.cod.AniBirth.volunteer.entity.Volunteer;
import com.cod.AniBirth.volunteer.entity.VolunteerApplication;
import com.cod.AniBirth.volunteer.entity.VolunteerReview;
import com.cod.AniBirth.volunteer.service.VolunteerApplicationService;
import com.cod.AniBirth.volunteer.service.VolunteerReviewService;
import com.cod.AniBirth.volunteer.service.VolunteerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/volunteer")
public class VolunteerController {
    private final MemberService memberService;
    private final VolunteerService volunteerService;
    private final CalendarService calendarService;
    private final VolunteerApplicationService volunteerApplicationService;
    private final VolunteerReviewService volunteerReviewService;

    // 전체 봉사 리스트
    @GetMapping("/list")
    public String volunteer(Authentication authentication, Model model,
                            @RequestParam(value = "page", defaultValue = "0") int page) {
        Page<Volunteer> paging = volunteerService.getAllVolunteer(page);

        Member member = null;

        if(authentication != null && authentication.isAuthenticated()) {
            member = memberService.findByUsername(authentication.getName());
        }

        model.addAttribute("member", member);
        model.addAttribute("paging", paging);

        return "volunteer/list";
    }

    // 봉사활동 등록
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create() {
        return "volunteer/create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@RequestParam("title") String title, @RequestParam("startDate") String startDate,
                         @RequestParam("endDate") String endDate, @RequestParam("deadLineDate") String deadLineDate,
                         @RequestParam("location") String location, @RequestParam("limit") int limit,
                         @RequestParam("thumbnailImg") MultipartFile thumbnailImg, @RequestParam("content") String content,
                         Principal principal) {
        String imageFileName = storeProfilePicture_v(thumbnailImg);

        Member member = memberService.getMemberByUsername(principal.getName());

        Volunteer volunteer = volunteerService.create(title, content, location, startDate, endDate, deadLineDate, imageFileName, limit, member, 0);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);

        calendarService.create(title, start, end, volunteer);

        return "redirect:/volunteer/list?createVolunteerSuccess=true";
    }

    // 봉사활동 수정
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, Model model) {
        Volunteer volunteer = volunteerService.getVolunteerById(id);

        model.addAttribute("volunteer", volunteer);

        return "volunteer/modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String modify(@PathVariable("id") Long id, @RequestParam("title") String title, @RequestParam("startDate") String startDate,
                         @RequestParam("endDate") String endDate, @RequestParam("deadLineDate") String deadLineDate,
                         @RequestParam("location") String location, @RequestParam("limit") int limit,
                         @RequestParam("thumbnailImg") MultipartFile thumbnailImg, @RequestParam("content") String content,
                         Principal principal) {
        String imageFileName = storeProfilePicture_v(thumbnailImg);

        // 등록한 보호소 회원
        Member member = memberService.getMemberByUsername(principal.getName());

        // 수정할 봉사활동
        Volunteer volunteer = volunteerService.getVolunteerById(id);

        // 특정 봉사활동에 대한 신청 리스트 가져오기
        List<VolunteerApplication> volunteerApplicationList = volunteerApplicationService.getAllById(id);

        volunteerService.modify(volunteer, title, content, location, startDate, endDate, deadLineDate, imageFileName, limit, member, volunteerApplicationList.size());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime start = LocalDateTime.parse(startDate, formatter);
        LocalDateTime end = LocalDateTime.parse(endDate, formatter);

        // 봉사활동에 맞는 캘린더 가져오기
        Calendar calendar = calendarService.getByVolunteer(volunteer);

        calendarService.modify(calendar, title, start, end, volunteer);

        return "redirect:/volunteer/list?modifySuccess=true";
    }

    // 봉사활동 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id) {
        Volunteer volunteer = volunteerService.getVolunteerById(id);

        volunteerService.delete(volunteer);

        return "redirect:/volunteer/list?deleteSuccess=true";
    }

    // 봉사 대표이미지 저장경로
    public String storeProfilePicture_v(MultipartFile profilePicture) {
        // 이미지 저장 디렉토리 경로
        String uploadDir = "C:\\work\\AniBirth\\src\\main\\resources\\static\\images\\volunteer";

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
        String imageFileName = fileName + ".png"; // 파일 확장자 지정
        // 파일을 저장합니다.
        try {
            Path filePath = uploadPath.resolve(imageFileName);
            Files.copy(profilePicture.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Could not store image file", e);
        }
        // 저장된 파일의 상대 경로를 반환합니다.
        return "/images/volunteer/" + imageFileName;
    }

    // 봉사활동 상세정보
    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id, Model model, Authentication authentication) {
        Volunteer volunteer = volunteerService.getVolunteerById(id);

        // String 날짜 변환하기(시작, 끝나는날짜)
        if (volunteer.getStartDate() != null) {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분");

            LocalDateTime startDateTime = LocalDateTime.parse(volunteer.getStartDate(), inputFormatter);
            LocalDateTime endDateTime = LocalDateTime.parse(volunteer.getEndDate(), inputFormatter);

            String formattedStartDateTime = startDateTime.format(outputFormatter);
            model.addAttribute("formattedStartDateTime", formattedStartDateTime);

            String formattedEndDateTime = endDateTime.format(outputFormatter);
            model.addAttribute("formattedEndDateTime", formattedEndDateTime);
        }

        // 마감날짜 변환
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate deadlineDate = LocalDate.parse(volunteer.getDeadLineDate(), formatter);

        // 날짜 비교
        boolean isDeadlinePassed = deadlineDate.isBefore(today);
        model.addAttribute("isDeadlinePassed", isDeadlinePassed);

        // 로그인된 회원 가져오기
        Member member = null;

        if(authentication != null && authentication.isAuthenticated()) {
            member = memberService.findByUsername(authentication.getName());
        }

        // 특정 봉사활동에 대한 신청 리스트 가져오기
        List<VolunteerApplication> volunteerApplicationList = volunteerApplicationService.getAllById(id);

        // 특정 봉사활동에 대한 회원 가져오기
        boolean alreadyApplied = volunteerApplicationService.existsByMemberAndVolunteer(member, volunteer);

        model.addAttribute("volunteer", volunteer);
        model.addAttribute("member", member);
        model.addAttribute("ApplicationList", volunteerApplicationList);
        model.addAttribute("alreadyApplied", alreadyApplied);

        return "volunteer/detail";
    }

    //봉사활동 신청
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/apply/{id}")
    public String apply(@PathVariable("id") Long id, Principal principal, Model model) {
        // 신청한 사람 가져오기
        Member member = memberService.getMemberByUsername(principal.getName());

        // 신청할 봉사 가져오기
        Volunteer volunteer = volunteerService.getVolunteerById(id);

        // 특정 봉사활동에 대한 신청 리스트 가져오기
        List<VolunteerApplication> volunteerApplicationList = volunteerApplicationService.getAllById(id);

        // 신청인원이 다 차면 안됨, 중복 신청 막기
        if(volunteerApplicationList.size() >= volunteer.getLimit()) {
            return "redirect:/volunteer/detail/%s?error=full".formatted(id);
        }

        volunteerApplicationService.create(member, volunteer);

        return "redirect:/volunteer/detail/%s?applySuccess=true".formatted(id);
    }

    // 봉사활동 후기
    @GetMapping("/review")
    public String reviewList(Authentication authentication, Model model,
                             @RequestParam(value = "page", defaultValue = "0") int page) {
        Member member = null;

        if(authentication != null && authentication.isAuthenticated()) {
            member = memberService.findByUsername(authentication.getName());
        }

        // 전체 후기 가져오기
        Page<VolunteerReview> paging = volunteerReviewService.getAll(page);

        model.addAttribute("member", member);
        model.addAttribute("paging", paging);

        return "volunteer/review";
    }

    // 후기 작성
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/review/create")
    public String reviewCreate() {
        return "volunteer/reviewCreate";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/review/create")
    public String reviewCreate(@RequestParam("title") String title, @RequestParam("body") String body,
                               @RequestParam("thumbnailImg") MultipartFile thumbnailImg,
                               @RequestParam(value = "subImg", required = false) List<MultipartFile> subImgs, Principal principal) {
        String imageFileName = storeProfilePicture_v(thumbnailImg);

        List<String> subImageNames = new ArrayList<>();
        if (subImgs != null) {
            for (MultipartFile subImg : subImgs) {
                if (!subImg.isEmpty()) {
                    String subImageName = storeProfilePicture_v(subImg);
                    subImageNames.add(subImageName);
                }
            }
        }

        Member member = memberService.getMemberByUsername(principal.getName());

        volunteerReviewService.create(title, body, 0, member, imageFileName, subImageNames);

        return "redirect:/volunteer/review?reviewCreateSuccess=true";
    }

    // 후기 디테일
    @GetMapping("/review/detail/{id}")
    public String reviewDetail(@PathVariable("id") Long id, Model model, Authentication authentication) {
        VolunteerReview volunteerReview = volunteerReviewService.getReviewById(id);

        Member member = null;

        if(authentication != null && authentication.isAuthenticated()) {
            member = memberService.findByUsername(authentication.getName());
        }

        volunteerReviewService.hit(volunteerReview);

        model.addAttribute("volunteerReview", volunteerReview);
        model.addAttribute("member", member);

        return "volunteer/reviewDetail";
    }

    // 후기 수정
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/review/modify/{id}")
    public String reviewModify(@PathVariable("id") Long id, Model model) {
        VolunteerReview volunteerReview = volunteerReviewService.getReviewById(id);

        model.addAttribute("volunteerReview", volunteerReview);

        return "volunteer/reviewModify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/review/modify/{id}")
    public String reviewModify(@PathVariable("id") Long id, Principal principal, @RequestParam("title") String title,
                               @RequestParam("body") String body, @RequestParam("thumbnailImg") MultipartFile thumbnailImg,
                               @RequestParam(value = "subImg", required = false) List<MultipartFile> subImgs) {
        Member member = memberService.getMemberByUsername(principal.getName());

        VolunteerReview volunteerReview = volunteerReviewService.getReviewById(id);

        String imageFileName = storeProfilePicture_v(thumbnailImg);

        List<String> subImageNames = new ArrayList<>();
        if (subImgs != null) {
            for (MultipartFile subImg : subImgs) {
                if (!subImg.isEmpty()) {
                    String subImageName = storeProfilePicture_v(subImg);
                    subImageNames.add(subImageName);
                }
            }
        }

        volunteerReviewService.modify(volunteerReview, member, title, body, imageFileName, subImageNames);

        return "redirect:/volunteer/review/detail/%d?reviewModifySuccess=true".formatted(id);
    }

    // 후기 삭제
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/review/delete/{id}")
    public String deleteReview(@PathVariable("id") Long id) {
        VolunteerReview volunteerReview = volunteerReviewService.getReviewById(id);

        volunteerReviewService.delete(volunteerReview);

        return "redirect:/volunteer/review?reviewDeleteSuccess=true";
    }
}
