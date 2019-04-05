package com.mymoneyapp.backend.service;

import com.mymoneyapp.backend.domain.BankingAccount;
import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.mapper.BankingAccountMapper;
import com.mymoneyapp.backend.repository.BankingAccountRepository;
import com.mymoneyapp.backend.request.BankingAccountRequest;
import com.mymoneyapp.backend.response.BankingAccountResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class BankingAccountService {

    @Autowired
    private BankingAccountMapper bankingAccountMapper;

    @Autowired
    private BankingAccountRepository bankingAccountRepository;

    @Transactional
    public Long save(final BankingAccountRequest bankingAccountRequest) {
        log.info("C=BankingAccountService, M=save, T=BankingAccountRequest {}", bankingAccountRequest);

        BankingAccount toPersist = bankingAccountMapper.requestToBankingAccount(bankingAccountRequest);
        BankingAccount persistedBankingAccount = persist(toPersist);
        return persistedBankingAccount.getId();
    }

    @Transactional(readOnly = true)
    public List<BankingAccountResponse> findAllByUser(final User user) {
        log.info("C=BankingAccountService, M=findAll, T=User {}", user);

        List<BankingAccount> bankingAccounts = bankingAccountRepository.findAllByUser(user);
        return bankingAccountMapper.bankingAccountsToResponses(bankingAccounts);
    }

    @Transactional
    protected BankingAccount persist(final BankingAccount bankingAccount) {
        log.info("C=BankingAccountService, M=persist, T=BankingAccount {}", bankingAccount);

        return bankingAccountRepository.save(bankingAccount);
    }
}
