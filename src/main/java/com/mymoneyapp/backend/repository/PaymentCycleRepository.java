package com.mymoneyapp.backend.repository;

import com.mymoneyapp.backend.domain.BankingAccount;
import com.mymoneyapp.backend.domain.PaymentCycle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface PaymentCycleRepository extends JpaRepository<PaymentCycle, Long>, JpaSpecificationExecutor<PaymentCycle> {

    Optional<PaymentCycle> findByIdAndBankingAccountIn(Long id, List<BankingAccount> bankingAccounts);

    List<PaymentCycle> findAllByBankingAccount(BankingAccount bankingAccount);

    List<PaymentCycle> findAllByBankingAccountIn(List<BankingAccount> bankingAccounts);

    Page<PaymentCycle> findAllByBankingAccountIn(List<BankingAccount> bankingAccounts,
                                                 Pageable pageable);

}
