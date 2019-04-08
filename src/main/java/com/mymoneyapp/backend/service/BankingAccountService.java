package com.mymoneyapp.backend.service;

import com.mymoneyapp.backend.domain.BankingAccount;
import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.exception.ResourceNotFoundException;
import com.mymoneyapp.backend.exception.UserIsNotDataOwnerException;
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
    public Long save(final User user, final BankingAccountRequest bankingAccountRequest) {
        log.info("C=BankingAccountService, M=save, T=BankingAccountRequest {}; User={}", bankingAccountRequest, user);

        BankingAccount toPersist = bankingAccountMapper.requestToBankingAccount(bankingAccountRequest);
        toPersist.setUser(user);
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
    public void update(final User user, final Long id, final BankingAccountRequest bankingAccountRequest) {
        log.info("C=BankingAccountService, M=update, T=ID {}; BankingAccountRequest {}", id, bankingAccountRequest);

        BankingAccount bankingAccount = retrieveById(user, id);

        bankingAccountMapper.updateBankingAccountFromRequest(bankingAccount, bankingAccountRequest);

        persist(bankingAccount);
    }

    @Transactional
    public void delete(final User user, final Long id) {
        log.info("C=BankingAccountService, M=delete, T=ID {}", id);

        retrieveById(user, id);

        bankingAccountRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    protected BankingAccount retrieveById(final User user, final Long id) {
        log.info("C=BankingAccountService, M=persist, T=User {}; ID {}", user, id);

        BankingAccount bankingAccount = bankingAccountRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        this.checkIfUserIsDataOwner(user, bankingAccount);

        return bankingAccount;
    }

    @Transactional
    protected BankingAccount persist(final BankingAccount bankingAccount) {
        log.info("C=BankingAccountService, M=persist, T=BankingAccount {}", bankingAccount);

        return bankingAccountRepository.save(bankingAccount);
    }

    private void checkIfUserIsDataOwner(final User user, final BankingAccount bankingAccount) {
        if (bankingAccount.getUser().getId() != user.getId()) {
            throw new UserIsNotDataOwnerException();
        }
    }

}
