package com.cod.AniBirth.animal.entity;

import com.cod.AniBirth.base.entity.BaseEntity;
import com.cod.AniBirth.category.entity.Category;
import com.cod.AniBirth.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper = true)
public class Animal extends BaseEntity {
    private String name;
    private String animalSeq;

    @Column(nullable = false)
    private String adoptionStatusCd = "1"; //입양상태,  1:공고중,2:입양가능,3:입양예정,4:입양완료,7:주인반환
    //Todo 입양날짜 생성, 입양보내는 멤버, 입양을 하는 멤버


    private String age;

    private String classification; //1:개 , 2:고양이,3:기타동물

    private String fileNm;
    private String filePath;
    private String foundPlace;

    private String gender;//1:암, 2:수

    private String gu;  //1:동구,2:중구,3:서구, 4:유성구,5:대덕구
    private String hairColor;
    private int hitCnt;
    private String memo;
    private String modDtTm;
    private String noticeDate;
    private String regDtTm;
    private String regId;
    private String rescueDate;
    private String species;

    private String weight;

    //Todo 입양날짜 생성
    @ManyToOne
    @JoinColumn(name = "adopter_id")
    private Member adopter; //입양 보내는 사람

    @ManyToOne
    @JoinColumn(name = "adoptee_id")
    private Member adoptee; //입양을 하는 사람

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category; // 카테고리 엔티티와의 관계

}
