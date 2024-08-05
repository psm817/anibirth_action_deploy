package com.cod.AniBirth.adopt;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class AdoptForm {
    @NotBlank(message="이름은 필수항목입니다.")
    @Size(max=5, message = "이름을 5자 이하로 입력해주세요.")
    private String name;

    @NotBlank(message="연락처는 필수항목입니다.")
    @Size(max=15, message = "연락처를 15자 이하로 입력해주세요.")
    private String phone;

    @NotBlank(message="이메일은 필수항목입니다.")
    @Email
    private String email;

    @NotBlank(message="나이는 필수항목입니다.")
    private String age;

    @NotBlank(message="직업/직장명은 필수항목입니다.")
    private String company; //직업,직장명

    private String postCode; //우편번호
    private String address; //주소
    private String detailAddress; //상세주소
    private String extraAddress; //참고사항

    private boolean isGender; //성별
    private boolean isMarried; //결혼여부

    @NotNull
    private MultipartFile file;

}
