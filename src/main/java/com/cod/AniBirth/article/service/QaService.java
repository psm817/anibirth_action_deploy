package com.cod.AniBirth.article.service;

import com.cod.AniBirth.article.entity.Article;
import com.cod.AniBirth.article.entity.Qa;
import com.cod.AniBirth.article.repository.QaRepository;
import com.cod.AniBirth.global.security.DataNotFoundException;
import com.cod.AniBirth.member.entity.Member;
import com.cod.AniBirth.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QaService {

    private final QaRepository qaRepository;
    private final MemberService memberService;

    public Page<Qa> getList(Pageable pageable) {
        return qaRepository.findAll(pageable);
    }

    public List<Qa> getList() {
        return qaRepository.findAll();
    }

    public Qa getQaById(Long id) {
        return qaRepository.findById(id).orElse(null);
    }

//    public Qa saveQa(Qa qa) {
//        if (qa.getId() != null) { // Q&A ID가 존재하면
//            Qa existingQa = qaRepository.findById(qa.getId()).orElse(null);
//            if (existingQa != null) {
//                existingQa.setTitle(qa.getTitle());
//                existingQa.setContent(qa.getContent());
//                existingQa.setModifyDate(LocalDateTime.now());
//                return qaRepository.save(existingQa);
//            }
//        }
//
//        return qaRepository.save(qa);
//    }

    public void saveQa(Qa qa) {
        // ID가 있는 엔티티는 기존에 존재하는 엔티티를 업데이트
        if (qa.getId() != null) {
            Qa existingQa = qaRepository.findById(qa.getId())
                    .orElseThrow(() -> new DataNotFoundException("QA not found"));
            existingQa.setTitle(qa.getTitle());
            existingQa.setContent(qa.getContent());
            // modifyDate는 자동으로 업데이트되므로, 직접 설정하지 않음
            qaRepository.save(existingQa);
        } else {
            // ID가 없으면 새로 추가
            qa.setCreateDate(LocalDateTime.now()); // `@CreatedDate`가 자동으로 처리할 수 있음
            qaRepository.save(qa);
        }
    }


//    public void saveQa(Qa qa) {
//        if (qa.getId() != null) {
//            qa.setModifyDate(LocalDateTime.now()); // 수정 시 updateDate 설정
//        }
//        qaRepository.save(qa);
//    }

    public void deleteQa(Long id) {
        qaRepository.deleteById(id);
    }

    public void create(String title, String content, String username) {
        Member member = memberService.findByUsername(username);
        Qa qa = Qa.builder()
                .title(title)
                .content(content)
                .createDate(LocalDateTime.now())
                .member(member)
                .build();

        qaRepository.save(qa);
    }

    public void increaseViewCount(Long id) {
        Qa qa = qaRepository.findById(id).orElseThrow(() -> new DataNotFoundException("QA not found"));
        qa.setViewCount(qa.getViewCount() + 1);
        qaRepository.save(qa);
    }
}
