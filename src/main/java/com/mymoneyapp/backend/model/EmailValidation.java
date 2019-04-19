package com.mymoneyapp.backend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class EmailValidation extends Email {

    public EmailValidation(String to, String toName) {
        this.emailType = EmailType.EMAIL_VALIDATION;
        this.to = to;
        this.toName = toName;
        this.subject = "Confirmação de registro de conta";
        this.emailImage = "https://ak9.picdn.net/shutterstock/videos/6831259/thumb/12.jpg";
        this.emailTitle = "Registro de conta";
        this.emailSubTitle = "Obrigado por criar sua nova conta no My Money App, ";
        this.emailSubTitleDescription = "Para usar a gama completa dos serviços My Money App, você precisará verificar o endereço de e-mail da sua conta.";
        this.emailMessageTitle = "O que é o My Money App?";
        this.emailMessage = "É uma pequena aplicação desenvolvida para ajudar a gerênciar multiplas"+
                " contas bancárias e a situação financeira" +
                " dos nossos usuários <br><br> Permite gerênciar os cartões de crédito e débito, notíficar" +
                " sobre vencimentos, além de claro, permitir a gestão das contas pessoais dos nossos clientes com total segurança e praticidade ";
    }
}
