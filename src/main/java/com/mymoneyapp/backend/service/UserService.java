package com.mymoneyapp.backend.service;

import com.mymoneyapp.backend.domain.EmailVerificationToken;
import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.exception.*;
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
    private HashService hashService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Long save(final UserRequest userRequest) {
        log.info("C=UserService, M=save, T=UserRequest.Email {}, UserRequest.Name, UserRequest.Password {}, UserRequest.ConfirmPassword {}", userRequest.getEmail(), userRequest.getName(), passwordEncoder.encode(userRequest.getPassword()),passwordEncoder.encode(userRequest.getConfirmPassword()));

        this.checkIfPasswordMatchConfirmPassword(userRequest);

        if(this.checkIfAlreadyHasUserByEmail(userRequest.getEmail())) throw new UserAlreadyRegistered();

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

    @Transactional
    protected HttpEntity unlockUser(String token) {
        log.info("C=UserService, M=unlockUser, T=Token {}", token);

        final EmailVerificationToken emailVerificationToken = emailService.retrieveEmailVerificationTokenByToken(token);
        User user = this.retrieveUserByEmail(emailVerificationToken.getUser().getEmail());
        user.setAccountNonLocked(true);
        this.persist(user);

        return ResponseEntity.ok("");
    }

    @Transactional(readOnly = true)
    protected User retrieveUserByEmail(final String email) {
        log.info("C=UserService, M=retrieveUserByEmail, T=Email {}", email);

        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    @Transactional(readOnly = true)
    protected boolean checkIfAlreadyHasUserByEmail(final String email) {
        log.info("C=UserService, M=checkAlreadyHasUserByEmail, T=Email {}", email);

        if(userRepository.findByEmail(email).orElse(null) != null) return true;
        return false;
    }

    @Transactional
    protected User persist(final User user) {
        log.info("C=UserService, M=persist, T=User {}", user);

        return userRepository.save(user);
    }

    public HttpEntity userValidationEmail(final String token) {
        log.info("C=UserService, M=validationUserEmail, T=TokenBase64 {}", token);

        final String email = hashService.decryptHash(token);
        return this.unlockUser(email);
    }

    public HttpEntity resendUserValidationEmail(final String email) {
        log.info("C=UserService, M=resendUserValidationEmail, T=Email {}", email);

        final User user = this.retrieveUserByEmail(email);
        if(!user.isAccountNonLocked()) {
            emailService.sendEmail(EmailType.EMAIL_VALIDATION, user);
            return ResponseEntity.ok("");
        }
        throw new UserAlreadyValidatedEmail();
    }

    public HttpEntity userForgetPassword(final String email) {
        log.info("C=UserService, M=userForgetPassword, T=Email {}", email);

        final User user = this.retrieveUserByEmail(email);
        if(user.isAccountNonLocked()) {
            emailService.sendEmail(EmailType.FORGET_PÀSSWORD, user);
            return ResponseEntity.ok("");
        }
        throw new UserIsAccountLocked();
    }

    public HttpEntity userForgetPassword(final UserChangePassRequest userChangePassRequest) {
        log.info("C=UserService, M=userForgetPassword, T=UserChangePassRequest.Password {}, UserChangePassRequest.ConfirmPassword {}, Token {} ", passwordEncoder.encode(userChangePassRequest.getPassword()), passwordEncoder.encode(userChangePassRequest.getConfirmPassword()), userChangePassRequest.getToken());

        this.checkIfPasswordMatchConfirmPassword(userChangePassRequest);

        final EmailVerificationToken emailVerificationToken = emailService.retrieveEmailVerificationTokenByToken(hashService.decryptHash(userChangePassRequest.getToken()));
        User toPersist = emailVerificationToken.getUser();

        if(!toPersist.isAccountNonLocked()) throw new UserIsAccountLocked();

        toPersist.setPassword(passwordEncoder.encode(userChangePassRequest.getPassword()));
        this.persist(toPersist);
        return ResponseEntity.ok("");
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
