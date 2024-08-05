package com.cod.AniBirth.article.entity;


import com.cod.AniBirth.base.entity.BaseEntity;
import com.cod.AniBirth.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Qa extends BaseEntity {

    private String title;
    private String content;
    private String adminComment; // 관리자 댓글
    private int viewCount; // 조회수 추가

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 작성자
}
