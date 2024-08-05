package com.cod.AniBirth.member.entity;

import com.cod.AniBirth.account.entity.Account;
import com.cod.AniBirth.animal.entity.Animal;
import com.cod.AniBirth.base.entity.BaseEntity;
import com.cod.AniBirth.donation.entity.Donation;
import com.cod.AniBirth.product.entity.Product;
import com.cod.AniBirth.volunteer.entity.Volunteer;
import com.cod.AniBirth.volunteer.entity.VolunteerApplication;
import com.cod.AniBirth.volunteer.entity.VolunteerReview;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.List;

@Getter
@Setter
@Entity
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseEntity {
    @Column(unique = true)
    private String username;

    private String password;
    private String nickname;    // 활동명 or 기업/보호소명

    @Column(unique = true)
    private String email;

    private String phone;
    private String address;
    private String thumbnailImg;

    private int authority;   // admin(0) or 보호소/기업(1) or 회원(2)
    private int isActive;   // 승인(1), 대기(0)


    @OneToOne(mappedBy = "member", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Account account;

    @OneToMany(mappedBy = "donor", cascade = CascadeType.REMOVE)
    private List<Donation> donationsAsDonor;

    @OneToMany(mappedBy = "recipient", cascade = CascadeType.REMOVE)
    private List<Donation> donationsAsRecipient;

    @OneToMany(mappedBy = "adopter", cascade = CascadeType.REMOVE)
    private List<Animal> animalsAdoter;

    @OneToMany(mappedBy = "adoptee", cascade = CascadeType.REMOVE)
    private List<Animal> animalsAdoptee;


    @OneToMany(mappedBy = "member")
    private List<VolunteerApplication> volunteerApplications;

    @OneToMany(mappedBy = "writer", cascade = CascadeType.REMOVE)
    private List<VolunteerReview> volunteerReviewList;


    @ManyToMany
    private List<Volunteer> appliedVolunteers; // 회원이 신청한 봉사 리스트

    @OneToMany(mappedBy = "member")
    private List<Product> products;




}
