package com.cod.AniBirth.adopt.repository;

import com.cod.AniBirth.adopt.entity.Adopt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdoptRepository extends JpaRepository<Adopt, Long> {
}
