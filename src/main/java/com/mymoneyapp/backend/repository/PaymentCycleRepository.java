package com.mymoneyapp.backend.repository;

import com.mymoneyapp.backend.domain.BankingAccount;
import com.mymoneyapp.backend.domain.PaymentCycle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentCycleRepository extends JpaRepository<PaymentCycle, Long> {

    List<PaymentCycle> findAllByBankingAccount(BankingAccount bankingAccount);

}
