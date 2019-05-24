package com.mymoneyapp.backend.service;

import com.mymoneyapp.backend.domain.AccessToken;
import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.exception.PasswordsNotMatchException;
import com.mymoneyapp.backend.exception.UserAccountLockedException;
import com.mymoneyapp.backend.exception.UserAccountNonLockedException;
import com.mymoneyapp.backend.exception.UserAlreadyRegisteredException;
import com.mymoneyapp.backend.exception.UserNotFoundException;
import com.mymoneyapp.backend.mapper.UserMapper;
import com.mymoneyapp.backend.model.EmailType;
import com.mymoneyapp.backend.repository.UserRepository;
import com.mymoneyapp.backend.request.UserChangePassRequest;
import com.mymoneyapp.backend.request.UserRequest;
import com.mymoneyapp.backend.response.UserResponse;
import com.mymoneyapp.backend.specification.UserSpecification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Long save(final UserRequest userRequest) {
        log.info("C=UserService, M=save");

        this.checkIfPasswordMatchConfirmPassword(userRequest);

        this.checkIfUserAlreadyRegistered(userRequest.getEmail());

        User toPersist = userMapper.requestToUser(userRequest);
        toPersist.setPassword(passwordEncoder.encode(toPersist.getPassword()));
        User persistedUser = persist(toPersist);

        emailService.sendEmail(EmailType.EMAIL_VALIDATION, persistedUser);

        return persistedUser.getId();
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> findAll(final UserSpecification userSpecification, final Pageable pageable) {
        log.info("C=UserService, M=findAll");

        Page<User> users = userRepository.findAll(userSpecification, pageable);
        return userMapper.usersToResponses(users);
    }

    @Transactional
    public void activateUserAccount(final String token) {
        log.info("C=UserService, M=validationUserEmail, P=Token {}", token);

        AccessToken accessToken = accessTokenService.retrieveByToken(token);
        User toPersist = retrieve(accessToken.getUser().getEmail());
        toPersist.setAccountNonLocked(true);
        persist(toPersist);
    }

    public void resendUserValidationEmail(final String email) {
        log.info("C=UserService, M=resendUserValidationEmail, P=Email {}", email);

        User user = retrieve(email);

        this.checkIfUserAccountLocked(user);

        emailService.sendEmail(EmailType.EMAIL_VALIDATION, user);
    }

    public void recoveryUserPassword(final String email) {
        log.info("C=UserService, M=recoveryUserPassword, P=Email {}", email);

        User user = retrieve(email);

        this.checkIfUserAccountNonLocked(user);

        emailService.sendEmail(EmailType.FORGET_PÀSSWORD, user);
    }

    public void changeUserPassword(final UserChangePassRequest changeUserPasswordRequest) {
        log.info("C=UserService, M=changeUserPassword");

        this.checkIfPasswordMatchConfirmPassword(changeUserPasswordRequest);

        AccessToken accessToken = accessTokenService.retrieveByToken(changeUserPasswordRequest.getToken());
        User toPersist = accessToken.getUser();

        this.checkIfUserAccountNonLocked(toPersist);

        toPersist.setPassword(passwordEncoder.encode(changeUserPasswordRequest.getPassword()));
        persist(toPersist);
    }

    @Transactional(readOnly = true)
    public User retrieve(final String email) {
        log.info("C=UserService, M=retrieveUserByEmail, P=Email {}", email);

        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    public User persist(final User user) {
        log.info("C=UserService, M=persist");

        return userRepository.save(user);
    }

    private void checkIfPasswordMatchConfirmPassword(final UserRequest userRequest) {
        if (!userRequest.getPassword().equals(userRequest.getConfirmPassword())) {
            throw new PasswordsNotMatchException();
        }
    }

    private void checkIfPasswordMatchConfirmPassword(final UserChangePassRequest changeUserPasswordRequest) {
        if (!changeUserPasswordRequest.getPassword().equals(changeUserPasswordRequest.getConfirmPassword())) {
            throw new PasswordsNotMatchException();
        }
    }

    @Transactional(readOnly = true)
    private void checkIfUserAlreadyRegistered(final String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new UserAlreadyRegisteredException();
        }
    }

    private void checkIfUserAccountLocked(final User user) {
        if (user.isAccountNonLocked()) {
            throw new UserAccountNonLockedException();
        }
    }

    private void checkIfUserAccountNonLocked(final User user) {
        if (!user.isAccountNonLocked()) {
            throw new UserAccountLockedException();
        }
    }

}
