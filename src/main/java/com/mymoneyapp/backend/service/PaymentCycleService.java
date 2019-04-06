package com.mymoneyapp.backend.service;

import com.mymoneyapp.backend.domain.BankingAccount;
import com.mymoneyapp.backend.domain.PaymentCycle;
import com.mymoneyapp.backend.mapper.PaymentCycleMapper;
import com.mymoneyapp.backend.repository.PaymentCycleRepository;
import com.mymoneyapp.backend.request.PaymentCycleRequest;
import com.mymoneyapp.backend.response.PaymentCycleResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class PaymentCycleService {

    @Autowired
    private PaymentCycleMapper paymentCycleMapper;

    @Autowired
    private PaymentCycleRepository paymentCycleRepository;

    @Transactional
    public Long save(final PaymentCycleRequest paymentCycleRequest) {
        log.info("C=PaymentCycleService, M=save, T=PaymentCycleRequest {}", paymentCycleRequest);

        PaymentCycle toPersist = paymentCycleMapper.requestToPaymentCycle(paymentCycleRequest);
        PaymentCycle persistedPaymentCycle = persist(toPersist);
        return persistedPaymentCycle.getId();
    }

    @Transactional
    protected PaymentCycle persist(final PaymentCycle paymentCycle) {
        log.info("C=PaymentCycleService, M=persist, T=PaymentCycle {}", paymentCycle);

        return paymentCycleRepository.save(paymentCycle);
    }

    @Transactional(readOnly = true)
    public List<PaymentCycleResponse> findAllByBankingAccount(final BankingAccount bankingAccount) {
        log.info("C=PaymentCycleService, M=findAll, T=BankingAccount {}", bankingAccount);

        List<PaymentCycle> paymentCycles = paymentCycleRepository.findAllByBankingAccount(bankingAccount);
        return paymentCycleMapper.paymentCyclesToResponses(paymentCycles);
    }

}
