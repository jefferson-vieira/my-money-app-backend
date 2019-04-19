package com.mymoneyapp.backend.service;

import com.mymoneyapp.backend.domain.EmailVerificationToken;
import com.mymoneyapp.backend.exception.EmailNotFoundException;
import com.mymoneyapp.backend.exception.EmailTokenHasExpiredException;
import com.mymoneyapp.backend.mapper.EmailVerificationTokenMapper;
import com.mymoneyapp.backend.model.Email;
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
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Locale;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private EmailVerificationTokenMapper emailVerificationTokenMapper;

    @Autowired
    private EmailVerificationTokenRepository emailVerificationTokenRepository;

    public void sendEmail(EmailType emailType, @AuthenticationPrincipal final User user) {
        log.info("C=EmailService, M=sendEmail; T=Emailtype {}, User {}", emailType, user);

        switch (emailType) {
            case EMAIL_VALIDATION:
                EmailValidation mail = new EmailValidation(user.getEmail(), user.getName());
                this.sendEmailValidation(mail, user);
                break;
            case FORGET_PÀSSWORD:
                break;
            case BILL_NOTIFICATION:
                break;
        }
    }

    private void sendEmailValidation(EmailValidation mail, final User user) {
        log.info("C=EmailService, M=sendEmailValidation; T=EmaiValidation {}, User {}", mail, user);

        try {
            final MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(mail.getTo());
            helper.setFrom(new InternetAddress(mail.getFrom(), mail.getFromName()));
            helper.setSubject(mail.getSubject());

            mail.setEmailLink("http://localhost:1800" + "/registration-confirm/" + this.encryptHash(this.save(user)));
            //mail.setEmailLink("https://meu-dinheiro-backend.herokuapp.com"+"/registration-confirm/"+this.encryptEmail(mail.getTo()));

            final String htmlContent = templateEngine.process("email.html", this.setContext(mail));
            helper.setText(htmlContent, true);
            javaMailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final String encryptHash(final String token) {
        log.info("C=EmailService, M=encryptHash; T=Token {}", token);

        try {
            return Base64.getUrlEncoder().encodeToString(token.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final String decryptHash(String token) {
        log.info("C=EmailService, M=decryptHash; T=TokenBase64 {}", token);

        try {
            return new String(Base64.getUrlDecoder().decode(token.getBytes("UTF-8")));
        }catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Transactional
    private final String save(final User user) {
        log.info("C=EmailService, M=save, T=User {}", user);

        EmailVerificationToken toPersist = emailVerificationTokenMapper.userToEmailVerificationToken(user, this.generateHash(user));
        EmailVerificationToken persistedEmailVerificationToken = this.persist(toPersist);

        return persistedEmailVerificationToken.getToken();
    }

    @Transactional(readOnly = true)
    protected EmailVerificationToken retrieveEmailVerificationTokenByToken(final String token) {
        log.info("C=EmailService, M=retrieveEmailVerificationTokenByToken, T=Token {}", token);

        EmailVerificationToken emailVerificationToken = emailVerificationTokenRepository.findByToken(token)
                .orElseThrow(EmailNotFoundException::new);

        this.checkIfTokenHasExpired(emailVerificationToken);

        emailVerificationToken.setEnabled(false);
        this.persist(emailVerificationToken);
        return emailVerificationToken;


    }

    @Transactional
    protected EmailVerificationToken persist(final EmailVerificationToken emailVerificationToken) {
        log.info("C=EmailService, M=persist; T=EmailVerificationToken {}", emailVerificationToken);

        return emailVerificationTokenRepository.save(emailVerificationToken);
    }

    private static Context setContext(Email mail) {
        log.info("C=EmailService, M=setContext; T=Email {}", mail);
        
        final Locale locale = new Locale("pt", "BR");
        final Context ctx = new Context(locale);

        ctx.setVariable("name", mail.getToName());
        ctx.setVariable("link", mail.getEmailLink());
        ctx.setVariable("image", mail.getEmailImage());
        ctx.setVariable("title", mail.getEmailTitle());
        ctx.setVariable("subtitle", mail.getEmailSubTitle());
        ctx.setVariable("subtitleDescription", mail.getEmailSubTitleDescription());
        ctx.setVariable("messageTitle", mail.getEmailMessageTitle());
        ctx.setVariable("message", mail.getEmailMessage());

        return ctx;
    }

    private final String generateHash(final User user) {
        log.info("C=EmailService, M=generateHash; T=User {}", user);

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
            final String toHash = user.toString()+ LocalDateTime.now().toString();
            messageDigest.update(toHash.getBytes("UTF-8"));
            final byte[] hashBytes = messageDigest.digest();
            StringBuffer hash = new StringBuffer();

            for (byte b : hashBytes)
                hash.append(String.format("%02x", b & 0xff));

            return hash.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void checkIfTokenHasExpired(final EmailVerificationToken emailVerificationToken) {
        final Integer expireInMinutes = 1;
        final LocalDateTime expiresIn = emailVerificationToken.getCreatedAt().plus(expireInMinutes, ChronoUnit.MINUTES);
        if(expiresIn.isBefore(LocalDateTime.now())) {
            throw  new EmailTokenHasExpiredException();
        }
    }

}


