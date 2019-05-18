package com.mymoneyapp.backend.service;

import com.mymoneyapp.backend.domain.AccessToken;
import com.mymoneyapp.backend.domain.User;
import com.mymoneyapp.backend.exception.EmailCannotBeSentException;
import com.mymoneyapp.backend.model.Email;
import com.mymoneyapp.backend.model.EmailForgetPassword;
import com.mymoneyapp.backend.model.EmailType;
import com.mymoneyapp.backend.model.EmailValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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
    private AccessTokenService accessTokenService;

    private static Context setEmailContext(final Email email) {
        log.info("C=EmailService, M=setContext; T=Email {}", email);

        final Locale locale = new Locale("pt", "BR");
        final Context context = new Context(locale);

        context.setVariable("name", email.getToName());
        context.setVariable("link", email.getEmailLink());
        context.setVariable("image", email.getEmailImage());
        context.setVariable("title", email.getEmailTitle());
        context.setVariable("subtitle", email.getEmailSubTitle());
        context.setVariable("subtitleDescription", email.getEmailSubTitleDescription());
        context.setVariable("messageTitle", email.getEmailMessageTitle());
        context.setVariable("message", email.getEmailMessage());
        return context;
    }

    public void sendEmail(final EmailType emailType, @AuthenticationPrincipal final User user) {
        log.info("C=EmailService, M=sendEmail; T=EmailType {}, User {}", emailType, user);

        AccessToken accessToken = accessTokenService.createAccessToken(user);
        Email email;
        if (emailType == EmailType.EMAIL_VALIDATION) {
            email = new EmailValidation(user.getEmail(), user.getName());
        } else {
            email = new EmailForgetPassword(user.getEmail(), user.getName());
        }
        email = this.addTokenToEmail(email, accessToken);
        this.sendEmail(this.prepareEmailToSend(email));
    }

    private void sendEmail(final MimeMessage message) {
        log.info("C=EmailService, M=sendEmail, T=MimeMessage {}", message);

        javaMailSender.send(message);
    }

    private Email addTokenToEmail(final Email email, final AccessToken accessToken) {
        log.info("C=EmailService, M=addTokenToEmail, T=Email {}, AccessToken {}", email, accessToken);

        email.setEmailLink(email.getEmailLink() + hashService.encryptHash(accessToken.getToken()));
        return email;
    }

    private MimeMessage prepareEmailToSend(final Email email) {
        log.info("C=EmailService, M=prepareEmailToSend, T=Email {}", email);

        try {
            final MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(email.getTo());
            helper.setFrom(new InternetAddress(email.getFrom(), email.getFromName()));
            helper.setSubject(email.getSubject());
            final String htmlContent = templateEngine.process("email.html", setEmailContext(email));
            helper.setText(htmlContent, true);
            return message;
        } catch (Exception e) {
            throw new EmailCannotBeSentException(e);
        }
    }
}


