package com.cod.AniBirth.member.service;

import com.cod.AniBirth.account.service.AccountService;
import com.cod.AniBirth.global.security.DataNotFoundException;

import com.cod.AniBirth.member.entity.Member;

import com.cod.AniBirth.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;


    public Member signup(String username, String password, String nickname, String email,
                         String phone, String address, String thumbnailImg, int authority, int isActive) {
        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .nickname(nickname)
                .email(email)
                .phone(phone)
                .address(address)
                .thumbnailImg(thumbnailImg)
                .authority(authority)
                .isActive(isActive)
                .createDate(LocalDateTime.now())
                .build();

        if(authority == 2) {
            member.setIsActive(1);
        }

        memberRepository.save(member);
        accountService.createOrUpdate(member, "", 0L);

        return member;
    }

    public boolean usernameExists(String username) {
        return memberRepository.existsByUsername(username);
    }



    public Member findByUsername(String name) {
        Optional<Member> member = memberRepository.findByUsername(name);

        if (member.isPresent()) {
            return member.get();
        } else {
            throw new DataNotFoundException("Member not found");
        }
    }

    public Member findByUsernameAndEmail(String id, String email) {
        return memberRepository.findByUsernameAndEmail(id, email);
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Member getCurrentMember() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = user.getUsername();
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }


    public List<Member> getAllMember() {
        return memberRepository.findAll();
    }

    public Member getMemberById(Long id) {
        Optional<Member> om = memberRepository.findById(id);

        if(om.isPresent()) {
            return om.get();
        } else {
            throw new DataNotFoundException("member not found");
        }
    }

    public void modify(Member member, String password, String nickname, String email, String phone, String address, String imageFileName) {
        member.setPassword(passwordEncoder.encode(password));
        member.setNickname(nickname);
        member.setEmail(email);
        member.setPhone(phone);
        member.setAddress(address);
        member.setThumbnailImg(imageFileName);

        memberRepository.save(member);
    }

    public void socialModify(Member member, String nickname, String email, String phone, String address, String imageFileName) {
        member.setNickname(nickname);
        member.setEmail(email);
        member.setPhone(phone);
        member.setAddress(address);
        member.setThumbnailImg(imageFileName);

        memberRepository.save(member);
    }
    public List<Member> getShelters() {
        return memberRepository.findByAuthority(1);

    }

    public Member getMemberByUsername(String name) {
        Optional<Member> om = memberRepository.findByUsername(name);

        if(om.isPresent()) {
            return om.get();
        } else {
            throw new DataNotFoundException("member not found");
        }
    }
}
