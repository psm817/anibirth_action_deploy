package com.cod.AniBirth.adopt.entity;

import com.cod.AniBirth.base.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Setter
@SuperBuilder
public class AdoptApply extends BaseEntity {

    private String name;
    private String phone;
    private String email;
    private String age;
    private String company; //직업,직장명

    private String postCode; //우편번호
    private String address; //주소
    private String detailAddress; //상세주소
    private String extraAddress; //참고사항

//    private boolean isGender; //성별
//    private boolean isMarried; //결혼여부
    private String gender; //성별
    private String marriedStatus; //결혼여부
    private String file;

}
