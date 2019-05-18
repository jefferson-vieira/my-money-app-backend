package com.mymoneyapp.backend.service;

import com.mymoneyapp.backend.domain.BankingAccount;
import com.mymoneyapp.backend.domain.Card;
import com.mymoneyapp.backend.domain.Transaction;
import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.exception.CardNotFoundException;
import com.mymoneyapp.backend.exception.TransactionNotFoundException;
import com.mymoneyapp.backend.mapper.CardMapper;
import com.mymoneyapp.backend.mapper.TransactionMapper;
import com.mymoneyapp.backend.repository.CardRepository;
import com.mymoneyapp.backend.repository.TransactionRepository;
import com.mymoneyapp.backend.request.CardRequest;
import com.mymoneyapp.backend.request.TransactionRequest;
import com.mymoneyapp.backend.response.TransactionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;

@Slf4j
@Service
public class TransactionService {

    @Autowired
    private CardService cardService;

    @Autowired
    private TransactionMapper transactionMapper;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public Long save(final User user, final TransactionRequest transactionRequest) {
        log.info("C=TransactionService, M=save, U={}, T=TransactionRequest {}", user, transactionRequest);

        Card card = cardService.retrieveById(transactionRequest.getCardId());

        Transaction toPersist = transactionMapper.requestToTransaction(transactionRequest);
        toPersist.setCard (card);
        Transaction persistedTransaction = persist(toPersist);
        return persistedTransaction.getId();
    }

    @Transactional(readOnly = true)
    public List<TransactionResponse> findAllByCardId(final Long id) {
        log.info("C=TransactionService, M=findAll, CardId={}", id);

        return transactionMapper.transactionsToResponses((retrieveAllByCardId(id)));
    }

    @Transactional(readOnly = true)
    protected Transaction retrieveById(final Long id) {
        log.info("C=TransactionService, M=retrieveById, T=CarId {}", id);

        return transactionRepository.findById(id).orElseThrow(TransactionNotFoundException::new);
    }

    @Transactional(readOnly = true)
    protected List<Transaction> retrieveAllByCard(final Card card) {
        log.info("C=TransactionService, M=retrieveAllByCard, T=Card {}", card);

        return transactionRepository.findAllByCard(card);
    }

    @Transactional(readOnly = true)
    protected List<Transaction> retrieveAllByCardId(final Long id) {
        log.info("C=TransactionService, M=retrieveAllByCardId, T=Card Id {}", id);

        return transactionRepository.findAllByCardId(id);
    }

    @Transactional(readOnly = true)
    protected List<Transaction> retrieveAllByCardsIn(final List<Card> cards) {
        log.info("C=TransactionService, M=retrieveAllByCardIn, T=Card {}", cards);

        return transactionRepository.findAllByCardIn(cards);
    }

    @Transactional
    protected Transaction persist(final Transaction transaction) {
        log.info("C=CardService, M=persist, T=Card {}", transaction);

        return transactionRepository.save(transaction);
    }

    @Transactional
    public void delete(final User user, final Long id) {
        log.info("C=TransactionService, M=delete, U={}, T=ID {}", user, id);

        this.retrieveById(id);

        transactionRepository.deleteById(id);
    }
}
