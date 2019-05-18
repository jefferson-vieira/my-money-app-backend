package com.mymoneyapp.backend.repository;

import com.mymoneyapp.backend.domain.Card;
import com.mymoneyapp.backend.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByCard (Card card);

    List<Transaction> findAllByCardId (Long id);

    List<Transaction> findAllByCardIn (List<Card> cards);
}
