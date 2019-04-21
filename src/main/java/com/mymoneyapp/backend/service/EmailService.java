package com.mymoneyapp.backend.service;

import com.mymoneyapp.backend.domain.EmailVerificationToken;
import com.mymoneyapp.backend.exception.EmailCannotBeSent;
import com.mymoneyapp.backend.exception.EmailTokenHasExpiredException;
import com.mymoneyapp.backend.exception.EmailTokenWasUsedException;
import com.mymoneyapp.backend.mapper.EmailVerificationTokenMapper;
import com.mymoneyapp.backend.model.Email;
import com.mymoneyapp.backend.model.EmailForgetPassword;
import com.mymoneyapp.backend.model.EmailType;
import com.mymoneyapp.backend.model.EmailValidation;
import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.repository.EmailVerificationTokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private HashService hashService;

    @Autowired
    private EmailVerificationTokenMapper emailVerificationTokenMapper;

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    public void sendEmail(EmailType emailType, @AuthenticationPrincipal final User user) {
        log.info("C=EmailService, M=sendEmail; T=EmailType {}, User {}", emailType, user);

        EmailVerificationToken emailVerificationToken = this.createEmailVerificationToken(user);
        this.save(emailVerificationToken);
        Email email;
        if(emailType == EmailType.EMAIL_VALIDATION) {
            email = new EmailValidation(user.getEmail(), user.getName());
        } else {
            email = new EmailForgetPassword(user.getEmail(), user.getName());
        }
        email = this.addTokenToEmail(email, emailVerificationToken);
        this.sendEmail(this.prepareEmailToSend(email));
    }

    private void sendEmail(MimeMessage message) {
        log.info("C=EmailService, M=sendEmail, T=MimeMessage {}", message);

        javaMailSender.send(message);
    }

    private Email addTokenToEmail(Email email, EmailVerificationToken token) {
        log.info("C=EmailService, M=addTokenToEmail, T=Email {}, EmailVerificationToken {}", email , token);

        email.setEmailLink(email.getEmailLink()+hashService.encryptHash(token.getToken()));
        return email;
    }

    private MimeMessage prepareEmailToSend(Email email) {
        log.info("C=EmailService, M=prepareEmailToSend, T=Email {}", email);

        try {
            final MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(email.getTo());
            helper.setFrom(new InternetAddress(email.getFrom(), email.getFromName()));
            helper.setSubject(email.getSubject());
            final String htmlContent = templateEngine.process("email.html", setContext(email));
            helper.setText(htmlContent, true);
            return message;
        } catch (Exception e) {
            throw new EmailCannotBeSent();
        }
    }

    @Transactional
    protected final String save(final EmailVerificationToken toPersist) {
        log.info("C=EmailService, M=save, T=EmailVerificationToken {}", toPersist);

        EmailVerificationToken persistedEmailVerificationToken = this.persist(toPersist);
        return persistedEmailVerificationToken.getToken();
    }

    @Transactional(readOnly = true)
    protected EmailVerificationToken retrieveEmailVerificationTokenByToken(final String token) {
        log.info("C=EmailService, M=retrieveEmailVerificationTokenByToken, T=Token {}", token);

        EmailVerificationToken emailVerificationToken = emailVerificationTokenRepository.findByToken(token)
                .orElseThrow(EmailTokenWasUsedException::new);

        this.checkIfTokenHasExpired(emailVerificationToken);

        emailVerificationToken.setEnabled(false);
        this.persist(emailVerificationToken);
        return emailVerificationToken;
    }

    @Transactional(readOnly = true)
    protected EmailVerificationToken retrieveEmailVerificationTokenByTokenWithoutExpiredTime(final String token) {
        log.info("C=EmailService, M=retrieveEmailVerificationTokenByToken, T=Token {}", token);

        EmailVerificationToken emailVerificationToken = emailVerificationTokenRepository.findByToken(token)
                .orElseThrow(EmailTokenWasUsedException::new);

        this.checkIfTokenHasExpired(emailVerificationToken);

        emailVerificationToken.setEnabled(false);
        this.persist(emailVerificationToken);
        return emailVerificationToken;
    }

    @Transactional
    protected EmailVerificationToken persist(final EmailVerificationToken token) {
        log.info("C=EmailService, M=persist; T=EmailVerificationToken {}", token);

        return emailVerificationTokenRepository.save(token);
    }

    private EmailVerificationToken createEmailVerificationToken(final User user) {
        log.info("C=EmailService, M=createEmailVerificationToken; T=User {}", user);

        final String hash = hashService.generateHash(user);
        return emailVerificationTokenMapper.userToEmailVerificationToken(user, hash);
    }

    private static Context setContext(Email email) {
        log.info("C=EmailService, M=setContext; T=Email {}", email);
        
        final Locale locale = new Locale("pt", "BR");
        final Context ctx = new Context(locale);

        ctx.setVariable("name", email.getToName());
        ctx.setVariable("link", email.getEmailLink());
        ctx.setVariable("image", email.getEmailImage());
        ctx.setVariable("title", email.getEmailTitle());
        ctx.setVariable("subtitle", email.getEmailSubTitle());
        ctx.setVariable("subtitleDescription", email.getEmailSubTitleDescription());
        ctx.setVariable("messageTitle", email.getEmailMessageTitle());
        ctx.setVariable("message", email.getEmailMessage());
        return ctx;
    }

    private void checkIfTokenHasExpired(final EmailVerificationToken token) {
        log.info("C=EmailService, M=checkIfTokenHasExpired; T=EmailVerificationToken {}", token);

        final Integer expireInMinutes = 15;
        final LocalDateTime expiresIn = token.getCreatedAt().plus(expireInMinutes, ChronoUnit.MINUTES);
        if(expiresIn.isBefore(LocalDateTime.now())) {
            token.setEnabled(false);
            this.persist(token);
            throw  new EmailTokenHasExpiredException();
        }
    }

}


