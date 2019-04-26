package com.mymoneyapp.backend.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Email {

    protected EmailType emailType;

    protected String from = "my-money-app0@gmail.com";

    protected String fromName = "Equipe My Money App";

    protected String to;

    protected String toName;

    protected String subject;

    protected String emailImage;

    protected String emailTitle;

    protected String emailSubTitle;

    protected String emailSubTitleDescription;

    protected String emailLink;

    protected String emailMessageTitle;

    protected String emailMessage;

}
