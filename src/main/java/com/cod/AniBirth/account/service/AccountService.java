package com.cod.AniBirth.account.service;

import com.cod.AniBirth.account.entity.Account;
import com.cod.AniBirth.account.repository.AccountRepository;
import com.cod.AniBirth.global.security.DataNotFoundException;
import com.cod.AniBirth.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Account findByMember(Member member) {
        Optional<Account> account = accountRepository.findByMember(member);

        if (account.isPresent()) {
            return account.get();
        } else {
            throw new DataNotFoundException("Account not found");
        }
    }

    @Transactional
    public Account createOrUpdate(Member member, String accountNum, Long aniPoint) {
        Optional<Account> existingAccount = accountRepository.findByMember(member);

        Account account;

        if(existingAccount.isPresent()) {
            account = existingAccount.get();
            account.setModifyDate(LocalDateTime.now());
        } else {
            account = Account.builder()
                    .member(member)
                    .account_number(accountNum)
                    .aniPoint(aniPoint)
                    .build();
        }

        return accountRepository.save(account);
    }
}
