package com.cod.AniBirth.volunteer.entity;

import com.cod.AniBirth.base.entity.BaseEntity;
import com.cod.AniBirth.calendar.entity.Calendar;
import com.cod.AniBirth.member.entity.Member;
import com.cod.global.util.HtmlUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class Volunteer extends BaseEntity {
    private String startDate;        // 봉사시작날짜
    private String endDate;          // 봉사끝나는날짜
    private String deadLineDate;     // 신청마감날짜
    private String location;                // 봉사 장소
    private String title;                   // 봉사명
    private String content;                 // 봉사내용

    private String thumbnailImg;            // 봉사사진
    private int limit;                      // 봉사최대인원 수
    private int applicant;                 // 신청인원수

    @ManyToOne
    private Member register;                // 봉사를 등록한 보호소(회원)

    @OneToMany(mappedBy = "volunteer", cascade = CascadeType.REMOVE)
    private List<VolunteerApplication> volunteerApplications;

    @OneToOne(mappedBy = "volunteer", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Calendar calendar;

    public String getFormattedBody() {
        return HtmlUtils.convertLineBreaksToHtml(content);
    }
}
