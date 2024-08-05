package com.cod.AniBirth.volunteer.service;

import com.cod.AniBirth.global.security.DataNotFoundException;
import com.cod.AniBirth.member.entity.Member;
import com.cod.AniBirth.volunteer.entity.Volunteer;
import com.cod.AniBirth.volunteer.entity.VolunteerApplication;
import com.cod.AniBirth.volunteer.repository.VolunteerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VolunteerService {
    private final VolunteerRepository volunteerRepository;

    public Volunteer create(String title, String content, String location, String startDate,
                            String endDate, String deadLineDate, String thumbnailImg, int limit, Member member, int applicant) {
        Volunteer volunteer = Volunteer.builder()
                .title(title)
                .content(content)
                .location(location)
                .startDate(startDate)
                .endDate(endDate)
                .deadLineDate(deadLineDate)
                .thumbnailImg(thumbnailImg)
                .limit(limit)
                .register(member)
                .applicant(applicant)
                .build();

        return volunteerRepository.save(volunteer);
    }

    public Page<Volunteer> getAllVolunteer(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 4, Sort.by(sorts));

        return volunteerRepository.findAll(pageable);
    }

    public Volunteer getVolunteerById(Long id) {
        Optional<Volunteer> ov = volunteerRepository.findById(id);

        if(ov.isPresent()) {
            return ov.get();
        } else {
            throw new DataNotFoundException("volunteer not found");
        }
    }

    public void modify(Volunteer volunteer, String title, String content, String location, String startDate,
                       String endDate, String deadLineDate, String imageFileName, int limit, Member member, int size) {
        volunteer.setTitle(title);
        volunteer.setContent(content);
        volunteer.setLocation(location);
        volunteer.setStartDate(startDate);
        volunteer.setEndDate(endDate);
        volunteer.setDeadLineDate(deadLineDate);
        volunteer.setThumbnailImg(imageFileName);
        volunteer.setLimit(limit);
        volunteer.setRegister(member);
        volunteer.setApplicant(size);
        volunteer.setModifyDate(LocalDateTime.now());

        volunteerRepository.save(volunteer);
    }

    public void delete(Volunteer volunteer) {
        volunteerRepository.delete(volunteer);
    }
}
