package com.mymoneyapp.backend.model;

import lombok.Data;

@Data
public abstract class Email {

    public EmailType emailType;

    public final String from = "my-money-app0@gmail.com";

    public final String fromName = "Equipe My Money App";

    public String to;

    public String toName;

    public String subject;

    public String emailImage;

    public String emailTitle;

    public String emailSubTitle;

    public String emailSubTitleDescription;

    public String emailLink;

    public String emailMessageTitle;

    public String emailMessage;

    public Email() {
    }
}
