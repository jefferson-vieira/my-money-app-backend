package com.mymoneyapp.backend.repository;

import com.mymoneyapp.backend.domain.BankingAccount;
import com.mymoneyapp.backend.domain.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findAllByBankingAccount(BankingAccount bankingAccount);

    List<Card> findAllByBankingAccountIn(List<BankingAccount> bankingAccounts);
}
