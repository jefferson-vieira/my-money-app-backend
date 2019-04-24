package com.mymoneyapp.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "flg_active <> false")
@SQLDelete(sql = "update user set flg_active = false where id = ?")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String number;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Builder.Default
    @Column(name = "flg_Active")
    private boolean enabled = true;

}
