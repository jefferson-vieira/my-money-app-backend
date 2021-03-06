package com.mymoneyapp.backend.service;

import com.mymoneyapp.backend.domain.BankingAccount;
import com.mymoneyapp.backend.domain.PaymentCycle;
import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.exception.ResourceNotFoundException;
import com.mymoneyapp.backend.mapper.BankingAccountMapper;
import com.mymoneyapp.backend.model.Summary;
import com.mymoneyapp.backend.repository.BankingAccountRepository;
import com.mymoneyapp.backend.request.BankingAccountRequest;
import com.mymoneyapp.backend.response.BankingAccountResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BankingAccountService {

    @Autowired
    private PaymentCycleService paymentCycleService;

    @Autowired
    private BankingAccountMapper bankingAccountMapper;

    @Autowired
    private BankingAccountRepository bankingAccountRepository;

    @Transactional
    public Long save(final User user, final BankingAccountRequest bankingAccountRequest) {
        log.info("C=BankingAccountService, M=save, U={}, T=BankingAccountRequest {}", user, bankingAccountRequest);

        BankingAccount toPersist = bankingAccountMapper.requestToBankingAccount(bankingAccountRequest);
        toPersist.setUser(user);
        BankingAccount persistedBankingAccount = persist(toPersist);
        return persistedBankingAccount.getId();
    }

    @Transactional(readOnly = true)
    public List<BankingAccountResponse> findAllByUser(final User user) {
        log.info("C=BankingAccountService, M=findAllByUser, U={}", user);

        List<BankingAccount> bankingAccounts = retrieveAllByUser(user);
        return bankingAccountMapper.bankingAccountsToResponses(bankingAccounts);
    }

    public Summary getSummary(final User user) {
        log.info("C=BankingAccountService, M=getSummary, U={}", user);

        List<BankingAccount> bankingAccounts = retrieveAllByUser(user);
        List<PaymentCycle> paymentCycles = paymentCycleService.retrieveAllByBankingAccountIn(bankingAccounts);
        return this.bankingAccountSummary(paymentCycles);
    }

    public Summary getSummary(final User user, final Long id) {
        log.info("C=BankingAccountService, M=getSummary, U={}, T=ID {}", user, id);

        BankingAccount bankingAccount = retrieveByIdAndUser(id, user);
        List<PaymentCycle> paymentCycles = paymentCycleService.retrieveAllByBankingAccount(bankingAccount);
        return this.bankingAccountSummary(paymentCycles);
    }

    @Transactional
    public void update(final User user, final Long id, final BankingAccountRequest bankingAccountRequest) {
        log.info("C=BankingAccountService, M=update, U={}, T=ID {}; BankingAccountRequest {}", user, id, bankingAccountRequest);

        BankingAccount bankingAccount = retrieveByIdAndUser(id, user);

        bankingAccountMapper.updateBankingAccountFromRequest(bankingAccount, bankingAccountRequest);

        persist(bankingAccount);
    }

    @Transactional
    public void delete(final User user, final Long id) {
        log.info("C=BankingAccountService, M=delete, U={}, T=ID {}", user, id);

        retrieveByIdAndUser(id, user);

        bankingAccountRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    protected BankingAccount retrieveByIdAndUser(final Long id, final User user) {
        log.info("C=BankingAccountService, M=retrieveByIdAndUser, U={}, T=ID {}", user, id);

        return bankingAccountRepository.findByIdAndUser(id, user)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    protected List<BankingAccount> retrieveAllByUser(final User user) {
        log.info("C=BankingAccountService, M=retrieveAllByUser, U={}", user);

        return bankingAccountRepository.findAllByUser(user);
    }

    @Transactional
    protected BankingAccount persist(final BankingAccount bankingAccount) {
        log.info("C=BankingAccountService, M=persist, T=BankingAccount {}", bankingAccount);

        return bankingAccountRepository.save(bankingAccount);
    }

    private Summary bankingAccountSummary(final List<PaymentCycle> paymentCycles) {
        List<Summary> summaries = paymentCycles.stream().map(this::paymentCycleSummary).collect(Collectors.toList());
        return new Summary(summaries.stream().map(Summary::getCredit).reduce(0D, Double::sum),
                summaries.stream().map(Summary::getDebit).reduce(0D, Double::sum));
    }

    private Summary paymentCycleSummary(final PaymentCycle pc) {
        return new Summary(pc.getCredits().stream().reduce(0D, (acc, c) -> acc + c.getValue(), Double::sum),
                pc.getDebits().stream().reduce(0D, (acc, d) -> acc + d.getValue(), Double::sum));
    }

}
