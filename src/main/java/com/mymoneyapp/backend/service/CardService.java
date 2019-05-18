package com.mymoneyapp.backend.service;

import com.mymoneyapp.backend.domain.BankingAccount;
import com.mymoneyapp.backend.domain.Card;
import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.exception.CardNotFoundException;
import com.mymoneyapp.backend.mapper.CardMapper;
import com.mymoneyapp.backend.repository.CardRepository;
import com.mymoneyapp.backend.request.CardRequest;
import com.mymoneyapp.backend.response.CardResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.NotBoundException;
import java.util.List;

@Slf4j
@Service
public class CardService {

    @Autowired
    private BankingAccountService bankingAccountService;

    @Autowired
    private CardMapper cardMapper;

    @Autowired
    private CardRepository cardRepository;

    @Transactional
    public Long save(final User user, final CardRequest cardRequest) {
        log.info("C=CardService, M=save, U={}, T=CardRequest {}", user, cardRequest);

        BankingAccount bankingAccount = bankingAccountService.retrieveByIdAndUser(cardRequest.getBankingAccountId(), user);

        Card toPersist = cardMapper.requestToCard(cardRequest);
        toPersist.setBankingAccount(bankingAccount);
        Card persistedCard = persist(toPersist);
        return persistedCard.getId();
    }

    @Transactional(readOnly = true)
    public List<CardResponse> findAllByUser(final User user) {
        log.info("C=CardService, M=findAll, U={}", user);

        return cardMapper.cardToResponses(retrieveAllByUser(user));
    }

    @Transactional(readOnly = true)
    protected Card retrieveById(final Long id) {
        log.info("C=CardService, M=retrieveById, T=CardId {}", id);

        return cardRepository.findById(id).orElseThrow(CardNotFoundException::new);
    }

    @Transactional(readOnly = true)
    protected List<Card> retrieveAllByBankingAccount(final BankingAccount bankingAccount) {
        log.info("C=CardService, M=retrieveAllByBankingAccount, T=BankingAccount {}", bankingAccount);

        return cardRepository.findAllByBankingAccount(bankingAccount);
    }

    @Transactional(readOnly = true)
    protected List<Card> retrieveAllByUser(final User user) {
        log.info("C=CardService, M=retrieveAllByUser, U={}", user);

        List<BankingAccount> bankingAccounts = bankingAccountService.retrieveAllByUser(user);
        return retrieveAllByBankingAccountIn(bankingAccounts);
    }

    @Transactional(readOnly = true)
    protected List<Card> retrieveAllByBankingAccountIn(final List<BankingAccount> bankingAccounts) {
        log.info("C=CardService, M=retrieveAllByBankingAccountIn, T=BankingAccounts {}", bankingAccounts);

        return cardRepository.findAllByBankingAccountIn(bankingAccounts);
    }

    @Transactional
    protected Card persist(final Card card) {
        log.info("C=CardService, M=persist, T=Card {}", card);

        return cardRepository.save(card);
    }

    @Transactional
    public void delete(final User user, final Long id) {
        log.info("C=CardService, M=delete, U={}, T=ID {}", user, id);

        this.retrieveById(id);

        cardRepository.deleteById(id);
    }
}
