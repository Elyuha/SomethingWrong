package com.javamaster.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {
    @Value("${spring.mail.host}")
    private String host;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.password}")
    private String password;

    @Value("${spring.mail.port}")
    private int port;

    @Value("${spring.mail.protocol}")
    private String protocol;

    @Value("${mail.debug}")
    private String debug;

    @Bean
    public JavaMailSender getMailSender(){
        JavaMailSenderImpl sender = new JavaMailSenderImpl();

        sender.setHost(host);
        sender.setPort(port);
        sender.setUsername(username);
        sender.setPassword(password);
        sender.setProtocol(protocol);

        Properties javaMailProperties = sender.getJavaMailProperties();
        javaMailProperties.setProperty("mail.transport.protocol", protocol);
        javaMailProperties.setProperty("mail.debug", debug);

        return sender;
    }
}
