package com.mymoneyapp.backend.repository;

import com.mymoneyapp.backend.domain.BankingAccount;
import com.mymoneyapp.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface BankingAccountRepository  extends JpaRepository<BankingAccount, Long>, JpaSpecificationExecutor<BankingAccount> {
//essa aqui nao faço a minima ideia do que faz
}
