package com.cod.AniBirth.volunteer.repository;

import com.cod.AniBirth.member.entity.Member;
import com.cod.AniBirth.volunteer.entity.Volunteer;
import com.cod.AniBirth.volunteer.entity.VolunteerApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerApplicationRepository extends JpaRepository<VolunteerApplication, Long> {
    List<VolunteerApplication> findByVolunteerId(Long id);

    boolean existsByMemberAndVolunteer(Member member, Volunteer volunteer);
}
