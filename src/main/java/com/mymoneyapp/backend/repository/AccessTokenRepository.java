package com.mymoneyapp.backend.repository;

import com.mymoneyapp.backend.domain.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    Optional<AccessToken> findByToken(String token);

}
