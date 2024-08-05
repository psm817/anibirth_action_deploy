package com.cod.AniBirth.account.repository;

import com.cod.AniBirth.account.entity.Account;
import com.cod.AniBirth.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByMember(Member member);
    Optional<Account> findByMemberId(Long memberId);
}
