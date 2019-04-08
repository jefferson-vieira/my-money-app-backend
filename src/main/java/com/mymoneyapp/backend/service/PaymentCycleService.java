package com.mymoneyapp.backend.service;

import com.mymoneyapp.backend.domain.PaymentCycle;
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

        this.checkIfPaymentCycleRequestHasCreditsOrDebits(paymentCycleRequest);

//        this.checkIfCreditsIsInPaymentCycleDuration();
//        this.checkIfDebitsIsInPaymentCycleDuration();

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
    public List<PaymentCycleResponse> findAll() {
        List<PaymentCycle> paymentCycles = paymentCycleRepository.findAll();
        return paymentCycleMapper.paymentCyclesToResponses(paymentCycles);
    }

//    @Transactional(readOnly = true)
//    public List<PaymentCycleResponse> findAllByBankingAccount(final BankingAccount bankingAccount) {
//        log.info("C=PaymentCycleService, M=findAll, T=BankingAccount {}", bankingAccount);
//
//        List<PaymentCycle> paymentCycles = paymentCycleRepository.findAllByBankingAccount(bankingAccount);
//        return paymentCycleMapper.paymentCyclesToResponses(paymentCycles);
//  }

    private void checkIfPaymentCycleRequestHasCreditsOrDebits(PaymentCycleRequest paymentCycleRequest) {
        if (paymentCycleRequest.getCredits().isEmpty() && paymentCycleRequest.getDebits().isEmpty()) {
            throw new PaymentCycleNotHasCreditsOrDebitsException();
        }
    }

}
