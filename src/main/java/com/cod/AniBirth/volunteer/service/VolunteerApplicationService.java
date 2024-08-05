package com.cod.AniBirth.volunteer.service;

import com.cod.AniBirth.member.entity.Member;
import com.cod.AniBirth.volunteer.entity.Volunteer;
import com.cod.AniBirth.volunteer.entity.VolunteerApplication;
import com.cod.AniBirth.volunteer.repository.VolunteerApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VolunteerApplicationService {
    private final VolunteerApplicationRepository volunteerApplicationRepository;

    public List<VolunteerApplication> getAll() {
        return volunteerApplicationRepository.findAll();
    }

    public List<VolunteerApplication> getAllById(Long id) {
        return volunteerApplicationRepository.findByVolunteerId(id);
    }

    public void create(Member member, Volunteer volunteer) {
        VolunteerApplication volunteerApplication = VolunteerApplication.builder()
                .member(member)
                .volunteer(volunteer)
                .build();

        volunteerApplicationRepository.save(volunteerApplication);
    }

    public boolean existsByMemberAndVolunteer(Member member, Volunteer volunteer) {
        return volunteerApplicationRepository.existsByMemberAndVolunteer(member, volunteer);
    }
}
