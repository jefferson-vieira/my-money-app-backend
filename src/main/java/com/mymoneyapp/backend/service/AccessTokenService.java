package com.mymoneyapp.backend.service;

import com.mymoneyapp.backend.domain.AccessToken;
import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.exception.AccessTokenHasExpiredException;
import com.mymoneyapp.backend.exception.AccessTokenWasUsedException;
import com.mymoneyapp.backend.mapper.AccessTokenMapper;
import com.mymoneyapp.backend.repository.AccessTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class AccessTokenService {

    @Autowired
    private HashService hashService;

    @Autowired
    private AccessTokenMapper accessTokenMapper;

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    public AccessToken createAccessToken(final User user) {
        log.info("C=AccessTokenService, M=createAccessToken; T=User {}", user);

        final String hash = hashService.generateHash(user);
        AccessToken accessToken = accessTokenMapper.userToAccessToken(user, hash);
        this.save(accessToken);
        return accessToken;
    }

    @Transactional
    protected final String save(final AccessToken toPersist) {
        log.info("C=AccessTokenService, M=save, T=AccessToken {}", toPersist);

        AccessToken persistedAccessToken = this.persist(toPersist);
        return persistedAccessToken.getToken();
    }

    @Transactional
    protected AccessToken persist(final AccessToken token) {
        log.info("C=AccessTokenService, M=persist; T=AccessToken {}", token);

        return accessTokenRepository.save(token);
    }

    @Transactional(readOnly = true)
    protected AccessToken retrieveAccessTokenByToken(final String token) {
        log.info("C=AccessTokenService, M=retrieveAccessTokenByToken, T=Token {}", token);

        AccessToken accessToken = accessTokenRepository.findByToken(token)
                .orElseThrow(AccessTokenWasUsedException::new);

        this.checkIfTokenHasExpired(accessToken);

        accessToken.setEnabled(false);
        this.persist(accessToken);
        return accessToken;
    }

    @Transactional
    public void checkIfTokenHasExpired(final AccessToken token) {
        log.info("C=AccessTokenService, M=checkIfTokenHasExpired; T=AccessToken {}", token);

        final Integer expireInMinutes = 15;
        final LocalDateTime expiresIn = token.getCreatedAt().plus(expireInMinutes, ChronoUnit.MINUTES);
        if(expiresIn.isBefore(LocalDateTime.now())) {
            token.setEnabled(false);
            this.persist(token);
            throw  new AccessTokenHasExpiredException();
        }
    }
}
