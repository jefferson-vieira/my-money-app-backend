package com.mymoneyapp.backend.repository;

import com.mymoneyapp.backend.domain.BankingAccount;
import com.mymoneyapp.backend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankingAccountRepository extends JpaRepository<BankingAccount, Long>, JpaSpecificationExecutor<BankingAccount> {

    List<BankingAccount> findAllByUser(User user);

}
