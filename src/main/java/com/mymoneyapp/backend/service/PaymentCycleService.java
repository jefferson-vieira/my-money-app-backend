package com.mymoneyapp.backend.service;

import com.mymoneyapp.backend.domain.BankingAccount;
import com.mymoneyapp.backend.domain.PaymentCycle;
import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.exception.PaymentCycleNotHasCreditsOrDebitsException;
import com.mymoneyapp.backend.exception.ResourceNotFoundException;
import com.mymoneyapp.backend.mapper.PaymentCycleMapper;
import com.mymoneyapp.backend.repository.PaymentCycleRepository;
import com.mymoneyapp.backend.request.PaymentCycleRequest;
import com.mymoneyapp.backend.response.PaymentCycleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class PaymentCycleService {

    @Autowired
    private BankingAccountService bankingAccountService;

    @Autowired
    private PaymentCycleMapper paymentCycleMapper;

    @Autowired
    private PaymentCycleRepository paymentCycleRepository;

    @Transactional
    public Long save(final User user, final PaymentCycleRequest paymentCycleRequest) {
        log.info("C=PaymentCycleService, M=save, U={}, T=PaymentCycleRequest {}", user, paymentCycleRequest);

        this.checkIfPaymentCycleRequestHasCreditsOrDebits(paymentCycleRequest);

        BankingAccount bankingAccount = bankingAccountService.retrieveByIdAndUser(paymentCycleRequest.getBankingAccount(), user);

        PaymentCycle toPersist = paymentCycleMapper.requestToPaymentCycle(paymentCycleRequest);
        toPersist.setBankingAccount(bankingAccount);
        PaymentCycle persistedPaymentCycle = persist(toPersist);
        return persistedPaymentCycle.getId();
    }

    @Transactional(readOnly = true)
    public Page<PaymentCycleResponse> findAll(final User user,
                                              final Pageable pageable) {
        log.info("C=PaymentCycleService, M=findAll, U={}", user);

        return paymentCycleMapper.paymentCyclesToResponses(retrieveAllByUser(user, pageable));
    }

    @Transactional(readOnly = true)
    public PaymentCycleResponse findById(final User user, final Long id) {
        log.info("C=PaymentCycleService, M=findById, U={}, T=ID ", user, id);

        List<BankingAccount> bankingAccounts = bankingAccountService.retrieveAllByUser(user);

        return paymentCycleMapper.paymentCycleToResponse(retrieveByIdAndBankingAccountIn(id, bankingAccounts));
    }

    @Transactional
    public void update(final User user, final Long id, final PaymentCycleRequest paymentCycleRequest) {
        log.info("C=PaymentCycleService, M=update, U={}, T=ID {}; PaymentCycleRequest {}", user, id, paymentCycleRequest);

        List<BankingAccount> bankingAccounts = bankingAccountService.retrieveAllByUser(user);

        PaymentCycle paymentCycle = retrieveByIdAndBankingAccountIn(id, bankingAccounts);

        paymentCycleMapper.updatePaymentCycleFromRequest(paymentCycle, paymentCycleRequest);

        persist(paymentCycle);
    }

    @Transactional
    public void delete(final User user, final Long id) {
        log.info("C=PaymentCycleService, M=delete, U={}, T=ID {}", user, id);

        List<BankingAccount> bankingAccounts = bankingAccountService.retrieveAllByUser(user);

        retrieveByIdAndBankingAccountIn(id, bankingAccounts);

        paymentCycleRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    protected Page<PaymentCycle> retrieveAllByUser(final User user,
                                                   final Pageable pageable) {
        log.info("C=PaymentCycleService, M=retrieveAllByUser, U={}", user);

        List<BankingAccount> bankingAccounts = bankingAccountService.retrieveAllByUser(user);
        return retrieveAllByBankingAccountIn(bankingAccounts, pageable);
    }

    @Transactional(readOnly = true)
    protected PaymentCycle retrieveByIdAndBankingAccountIn(final Long id, final List<BankingAccount> bankingAccounts) {
        log.info("C=PaymentCycleService, M=retrieveByIdAndBankingAccountIn, T=ID {}; BankingAccounts {}", id, bankingAccounts);

        return paymentCycleRepository.findByIdAndBankingAccountIn(id, bankingAccounts)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Transactional(readOnly = true)
    protected List<PaymentCycle> retrieveAllByBankingAccount(final BankingAccount bankingAccount) {
        log.info("C=PaymentCycleService, M=retrieveAllByBankingAccount, T=BankingAccount {}", bankingAccount);

        return paymentCycleRepository.findAllByBankingAccount(bankingAccount);
    }

    @Transactional(readOnly = true)
    protected List<PaymentCycle> retrieveAllByBankingAccountIn(final List<BankingAccount> bankingAccounts) {
        log.info("C=PaymentCycleService, M=retrieveAllByBankingAccountIn, T=BankingAccounts {}", bankingAccounts);

        return paymentCycleRepository.findAllByBankingAccountIn(bankingAccounts);
    }

    @Transactional(readOnly = true)
    protected Page<PaymentCycle> retrieveAllByBankingAccountIn(final List<BankingAccount> bankingAccounts,
                                                               final Pageable pageable) {
        log.info("C=PaymentCycleService, M=retrieveAllByBankingAccountIn, T=BankingAccounts {}", bankingAccounts);

        return paymentCycleRepository.findAllByBankingAccountIn(bankingAccounts, pageable);
    }

    @Transactional
    protected PaymentCycle persist(final PaymentCycle paymentCycle) {
        log.info("C=PaymentCycleService, M=persist, T=PaymentCycle {}", paymentCycle);

        return paymentCycleRepository.save(paymentCycle);
    }

    private void checkIfPaymentCycleRequestHasCreditsOrDebits(final PaymentCycleRequest paymentCycleRequest) {
        if ((paymentCycleRequest.getCredits() == null || paymentCycleRequest.getCredits().isEmpty())
                && (paymentCycleRequest.getDebits() == null || paymentCycleRequest.getDebits().isEmpty())) {
            throw new PaymentCycleNotHasCreditsOrDebitsException();
        }
    }

}
