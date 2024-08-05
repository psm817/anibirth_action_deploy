package com.cod.AniBirth.volunteer.service;

import com.cod.AniBirth.global.security.DataNotFoundException;
import com.cod.AniBirth.member.entity.Member;
import com.cod.AniBirth.volunteer.entity.VolunteerReview;
import com.cod.AniBirth.volunteer.repository.VolunteerReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VolunteerReviewService {
    private final VolunteerReviewRepository volunteerReviewRepository;

    public void create(String title, String body, int hit, Member member, String thumbnailImg, List<String> subImages) {
        VolunteerReview volunteerReview = VolunteerReview.builder()
                .title(title)
                .body(body)
                .hit(hit)
                .writer(member)
                .thumbnailImg(thumbnailImg)
                .subImages(subImages)
                .build();

        volunteerReviewRepository.save(volunteerReview);
    }

    public Page<VolunteerReview> getAll(int page) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("createDate"));
        Pageable pageable = PageRequest.of(page, 6, Sort.by(sorts));

        return volunteerReviewRepository.findAll(pageable);
    }

    public VolunteerReview getReviewById(Long id) {
        Optional<VolunteerReview> ovr = volunteerReviewRepository.findById(id);

        if(ovr.isPresent()) {
            return ovr.get();
        } else {
            throw new DataNotFoundException("review not found");
        }
    }

    public void hit(VolunteerReview volunteerReview) {
        volunteerReview.setHit(volunteerReview.getHit() + 1);

        volunteerReviewRepository.save(volunteerReview);
    }

    public void modify(VolunteerReview volunteerReview, Member member, String title, String body, String imageFileName, List<String> subImageNames) {
        volunteerReview.setWriter(member);
        volunteerReview.setTitle(title);
        volunteerReview.setBody(body);
        volunteerReview.setThumbnailImg(imageFileName);
        volunteerReview.setSubImages(subImageNames);

        volunteerReviewRepository.save(volunteerReview);
    }

    public void delete(VolunteerReview volunteerReview) {
        volunteerReviewRepository.delete(volunteerReview);
    }
}
