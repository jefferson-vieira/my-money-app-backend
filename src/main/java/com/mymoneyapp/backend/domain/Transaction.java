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
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "flg_active <> false")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private CardType transactionType;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Double value;

    @Column(nullable = false)
    private short installment;

    @Column(nullable = false)
    private LocalDate dateTransaction;

    @ManyToOne(optional = false)
    @JoinColumn(name="card_id")
    private Card card;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Builder.Default
    @Column(name = "flg_Active")
    private boolean enabled = true;
}
