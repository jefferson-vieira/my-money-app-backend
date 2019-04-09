package com.mymoneyapp.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "flg_active <> false")
@SQLDelete(sql = "update payment_cycle set flg_active = false where id = ?")
public class PaymentCycle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDate date;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Credit> credits;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Debit> debits;

    @ManyToOne(optional = false)
    private BankingAccount bankingAccount;

    @Builder.Default
    @Column(name = "flg_Active")
    private boolean enabled = true;
}
