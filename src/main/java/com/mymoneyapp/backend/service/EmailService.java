package com.mymoneyapp.backend.service;

import com.mymoneyapp.backend.model.Email;
import com.mymoneyapp.backend.model.EmailType;
import com.mymoneyapp.backend.model.EmailValidation;
import com.mymoneyapp.backend.domain.User;
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
import java.util.Base64;
import java.util.Locale;

@Slf4j
@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine templateEngine;

    public void sendEmail(EmailType emailType, @AuthenticationPrincipal final User user) {
        log.info("C=EmailService, M=sendEmail; T=Emailtype {}, User {}", emailType, user);

        switch (emailType) {
            case EMAIL_VALIDATION:
                EmailValidation mail = new EmailValidation(user.getEmail(), user.getName());
                this.sendEmailValidation(mail);
                break;
            case FORGET_PÀSSWORD:
                break;
            case BILL_NOTIFICATION:
                break;
        }

    }

    private void sendEmailValidation(EmailValidation mail) {
        log.info("C=EmailService, M=sendEmailValidation; T=EmaiValidation {}", mail);

        try {
            final MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

            helper.setTo(mail.getTo());
            helper.setFrom(new InternetAddress(mail.getFrom(), mail.getFromName()));
            helper.setSubject(mail.getSubject());

            mail.setEmailLink("http://localhost:1800" + "/users/validate/" + this.encryptEmail(mail.getTo()));
            //mail.setEmailLink("https://meu-dinheiro-backend.herokuapp.com"+"/users/validate/"+this.encryptEmail(mail.getTo()));

            final String htmlContent = templateEngine.process("email.html", this.setContext(mail));

            helper.setText(htmlContent, true);

            javaMailSender.send(message);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String encryptEmail(String email) {
        log.info("C=EmailService, M=encryptEmail; T=Email {}", email);

        final String encodedEmail = "Seu_babaca_pau_no_cu#" + email + "#por_que_ta_dando_decode_no_meu_base_64_?";
        final String strData = Base64.getUrlEncoder().encodeToString(encodedEmail.getBytes());

        return strData;
    }

    public String decryptEmail(String emailEncrypted) {
        log.info("C=EmailService, M=decryptEmail; T=EncodedEmail {}", emailEncrypted);

        final String decodeEmail = new String(Base64.getUrlDecoder().decode(emailEncrypted.getBytes()));
        final String[] decodedEmail = decodeEmail.split("#");
        final String decryptedEmail = decodedEmail[1];

        log.info("C=EmailService, M=decryptEmail; T=DecodedEmail {}", decryptedEmail);

        return decryptedEmail;
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

}


