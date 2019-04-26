package com.mymoneyapp.backend.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Where(clause = "flg_active <> false")
@SQLDelete(sql = "update email_request set flg_active = false where id = ?")
public class AccessToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(optional = false)
    private User user;

    @Builder.Default
    @Column(name = "flg_Active")
    private boolean enabled = true;
}
