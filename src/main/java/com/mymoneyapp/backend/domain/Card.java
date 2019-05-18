package com.mymoneyapp.backend.domain;

import com.mymoneyapp.backend.model.CardType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "flg_active <> false")
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cardNumber;

    @Column(nullable = false)
    private String CardFlag;

    @Column(nullable = false)
    private String cardOwnerName;

    @Column(nullable = false)
    private short cardCode;

    @Column(nullable = false)
    private LocalDate cardExpiration;

    @Column(nullable = false)
    private CardType cardType;

    @ManyToOne(optional = false)
    @JoinColumn(name="bankingAccount_id")
    private BankingAccount bankingAccount;

    @OneToMany(mappedBy = "card")
    private List<Transaction> transactions;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder.Default
    @Column(name = "flg_Active")
    private boolean enabled = true;
}
