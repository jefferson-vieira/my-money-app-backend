package com.mymoneyapp.backend.service;

import com.mymoneyapp.backend.domain.EmailVerificationToken;
import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.exception.EmailNotFoundException;
import com.mymoneyapp.backend.exception.PasswordsNotMatchException;
import com.mymoneyapp.backend.exception.UserIsAccountLocked;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
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
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Long save(final UserRequest userRequest) {
        log.info("C=UserService, M=save, T=UserRequest {}", userRequest);

        this.checkIfPasswordMatchConfirmPassword(userRequest);

        User toPersist = userMapper.requestToUser(userRequest);
        toPersist.setPassword(passwordEncoder.encode(toPersist.getPassword()));
        User persistedUser = persist(toPersist);

        emailService.sendEmail(EmailType.EMAIL_VALIDATION, persistedUser);

        return persistedUser.getId();
    }

    @Transactional(readOnly = true)
    public Page<UserResponse> findAll(final UserSpecification userSpecification, final Pageable pageable) {
        log.info("C=UserService, M=findAll, T=UserSpecification {}; Pageable {}", userSpecification, pageable);

        Page<User> users = userRepository.findAll(userSpecification, pageable);
        return userMapper.usersToResponses(users);
    }

    @Transactional(readOnly = true)
    protected User retrieveUserByEmail(final String email) {
        log.info("C=UserService, M=retrieveUserByEmail, T=Email {}", email);

        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional
    protected User persist(final User user) {
        log.info("C=UserService, M=persist, T=User {}", user);

        return userRepository.save(user);
    }

    public HttpEntity validationUserEmail(final String token) {
        log.info("C=UserService, M=validationUserEmail, T=TokenBase64 {}", token);

        final String email = emailService.decryptHash(token);
        return this.unlockUser(email);
    }

    @Transactional
    protected HttpEntity unlockUser(String token) {
        log.info("C=UserService, M=unlockUser, T=Token {}", token);

        final EmailVerificationToken emailVerificationToken = emailService.retrieveEmailVerificationTokenByToken(token);
        User user = userRepository.findByEmail(emailVerificationToken.getUser().getEmail())
                .orElseThrow(EmailNotFoundException::new);
        user.setAccountNonLocked(true);
        this.persist(user);
        return ResponseEntity.ok("Message: email successfully confirmed");
    }

    public HttpEntity userForgetPassword(final String email) {
        log.info("C=UserService, M=userForgetPassword, T=Email {}", email);

        final User user = this.retrieveUserByEmail(email);
        if(user.isAccountNonLocked()) this.emailService.sendEmail(EmailType.FORGET_PÀSSWORD, user);
        return ResponseEntity.ok("Message: If the user exists and is checked we'll send a email to you");
    }

    public HttpEntity userForgetPassword(final UserChangePassRequest userChangePassRequest) {
        log.info("C=UserService, M=userForgetPassword, T=UserChangePassRequest {}", userChangePassRequest);

        this.checkIfPasswordMatchConfirmPassword(userChangePassRequest);

        final EmailVerificationToken emailVerificationToken = emailService.retrieveEmailVerificationTokenByToken(emailService.decryptHash(userChangePassRequest.getToken()));
        User toPersist = emailVerificationToken.getUser();

        if(!toPersist.isAccountNonLocked()) throw new UserIsAccountLocked();

        toPersist.setPassword(passwordEncoder.encode(userChangePassRequest.getPassword()));
        this.persist(toPersist);

        return ResponseEntity.ok("Message: Your password was changed successful");
    }

    private void checkIfPasswordMatchConfirmPassword(final UserRequest userRequest) {
        if (!userRequest.getPassword().equals(userRequest.getConfirmPassword())) {
            throw new PasswordsNotMatchException();
        }
    }

    private void checkIfPasswordMatchConfirmPassword(final UserChangePassRequest userChangePassRequest) {
        if (!userChangePassRequest.getPassword().equals(userChangePassRequest.getConfirmPassword())) {
            throw new PasswordsNotMatchException();
        }
    }

}
