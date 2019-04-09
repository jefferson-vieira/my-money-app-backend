package com.mymoneyapp.backend.service;

import com.mymoneyapp.backend.domain.BankingAccount;
import com.mymoneyapp.backend.domain.PaymentCycle;
import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.exception.PaymentCycleNotHasCreditsOrDebitsException;
import com.mymoneyapp.backend.mapper.PaymentCycleMapper;
import com.mymoneyapp.backend.repository.PaymentCycleRepository;
import com.mymoneyapp.backend.request.PaymentCycleRequest;
import com.mymoneyapp.backend.response.PaymentCycleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
        log.info("C=PaymentCycleService, M=save, T=PaymentCycleRequest {}", paymentCycleRequest);

        this.checkIfPaymentCycleRequestHasCreditsOrDebits(paymentCycleRequest);

        BankingAccount bankingAccount = bankingAccountService.retrieveById(user, paymentCycleRequest.getBankingAccountId());

        PaymentCycle toPersist = paymentCycleMapper.requestToPaymentCycle(paymentCycleRequest);
        toPersist.setBankingAccount(bankingAccount);
        PaymentCycle persistedPaymentCycle = persist(toPersist);
        return persistedPaymentCycle.getId();
    }

    @Transactional(readOnly = true)
    public List<PaymentCycleResponse> findAll(final User user) {
        return paymentCycleMapper.paymentCyclesToResponses(retrieveAllByUser(user));
    }

    @Transactional(readOnly = true)
    public List<PaymentCycle> retrieveAllByUser(final User user) {
        List<BankingAccount> bankingAccounts = bankingAccountService.retrieveAllByUser(user);

        return bankingAccounts
                .stream().map(this::retrieveAllByBankingAccount).flatMap(List::stream)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PaymentCycle> retrieveAllByBankingAccount(final BankingAccount bankingAccount) {
        return paymentCycleRepository.findAllByBankingAccount(bankingAccount);
    }

    @Transactional
    protected PaymentCycle persist(final PaymentCycle paymentCycle) {
        log.info("C=PaymentCycleService, M=persist, T=PaymentCycle {}", paymentCycle);

        return paymentCycleRepository.save(paymentCycle);
    }

    private void checkIfPaymentCycleRequestHasCreditsOrDebits(PaymentCycleRequest paymentCycleRequest) {
        if (paymentCycleRequest.getCredits().isEmpty() && paymentCycleRequest.getDebits().isEmpty()) {
            throw new PaymentCycleNotHasCreditsOrDebitsException();
        }
    }

}
