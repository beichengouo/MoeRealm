package com.moerealm.service;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    public void sendVerificationEmail(String to, String subject, String message) throws EmailException {
        HtmlEmail email = new HtmlEmail();
        email.setHostName(host);
        email.setSmtpPort(port);
        email.setAuthentication(username, password);
        email.setSSLOnConnect(true);
        email.setFrom(username);
        email.addTo(to);
        email.setSubject(subject);
        email.setHtmlMsg(message);
        email.send();
    }
}

