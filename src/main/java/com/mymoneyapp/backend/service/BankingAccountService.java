package com.mymoneyapp.backend.service;

import com.mymoneyapp.backend.domain.BankingAccount;
import com.mymoneyapp.backend.repository.BankingAccountRepository;
import com.mymoneyapp.backend.request.BankingAccountRequest;
import com.mymoneyapp.backend.mapper.BankingAccountMapper;
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

        BankingAccount toPersist = bankingAccountMapper.requestTobankingAccount(bankingAccountRequest);
        BankingAccount persistedUser = persist(toPersist);
        return persistedUser.getId();
    }

    @Transactional(readOnly = true)
    public List<BankingAccountResponse> findAll() {
        log.info("C=BankingAccountService, M=findAll");

        List<BankingAccount> bankingAccounts = bankingAccountRepository.findAll();
        return bankingAccountMapper.bankingAccountToResponse(bankingAccounts);
    }

    @Transactional
    protected BankingAccount persist(final BankingAccount bankingAccount) {
        log.info("C=BankingAccountService, M=persist, T=BankingAccount {}", user);

        return bankingAccountRepository.save(bankingAccount);
    }
}
