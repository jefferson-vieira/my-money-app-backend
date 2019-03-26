package com.mymoneyapp.backend.service;

import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.exception.PasswordsNotMatchException;
import com.mymoneyapp.backend.exception.UserNotFoundException;
import com.mymoneyapp.backend.mapper.UserMapper;
import com.mymoneyapp.backend.repository.UserRepository;
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

    private void checkIfPasswordMatchConfirmPassword(final UserRequest userRequest) {
        if (!userRequest.getPassword().equals(userRequest.getConfirmPassword())) {
            throw new PasswordsNotMatchException();
        }
    }

}
