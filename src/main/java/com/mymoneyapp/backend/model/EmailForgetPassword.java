package com.mymoneyapp.backend.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
public class EmailForgetPassword extends Email {

    public EmailForgetPassword(String to, String toName) {
        this.emailType = EmailType.FORGET_PÀSSWORD;
        this.to = to;
        this.toName = toName;
        this.subject = "Redefinição de senha";
        this.emailImage = "https://ak9.picdn.net/shutterstock/videos/6831259/thumb/12.jpg";
        this.emailTitle = "Esqueceu a senha?";
        this.emailSubTitle = "Alguém solicitou a troca da sua senha, ";
        this.emailSubTitleDescription = "Se você não solicitou a redefinição da senha, ignore este e-mail. Nenhuma alteração será realizada. Mas se você solicitou, clique em verificar agora";
        this.emailMessageTitle = "Como criar uma senha segura?";
        this.emailMessage = "Sugerimos uma senha fácil de lembrar, mas que seja a mais forte possível. Tente não usar palavras que se encontram no dicionário, mas use uma combinação de letras maiúsculas e minúsculas, juntamente com números e/ou caracteres especiais.";
    }
}
