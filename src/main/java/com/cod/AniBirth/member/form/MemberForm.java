package com.cod.AniBirth.member.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MemberForm {
    @Size(min = 3, max = 25)
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String nickname;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String phone;

    @NotBlank
    private String address;

    // 이미지 업로드를 위한 필드 추가
    private MultipartFile thumbnailImg;

    @Setter
    @Getter
    private int authority;   // admin(0) or 보호소/기업(1) or 회원(2)

    @Getter
    @Setter
    private int isActive;   // 승인(1), 대기(0)

}
