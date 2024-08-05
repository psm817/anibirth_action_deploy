package com.cod.AniBirth.adopt.repository;

import com.cod.AniBirth.adopt.entity.AdoptApply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdoptApplyRepository extends JpaRepository<AdoptApply, Long> {
}
