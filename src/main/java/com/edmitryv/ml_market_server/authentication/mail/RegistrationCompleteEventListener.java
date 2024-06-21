package com.edmitryv.ml_market_server.authentication.mail;

import com.edmitryv.ml_market_server.authentication.services.AuthenticationService;
import com.edmitryv.ml_market_server.core.models.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Properties;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    @Autowired
    private final AuthenticationService authenticationService;
    private User theUser;
    @Value("${email.address}")
    private String address;
    @Value("${email.password}")
    private String password;
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername(address);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        theUser = event.getUser();
        String verificationToken = UUID.randomUUID().toString();
        authenticationService.saveUserVerificationToken(theUser, verificationToken);
        String url = event.getApplicationUrl() + "/authentication/registration/verify-email?token=" + verificationToken;
        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("Click the link to verify your registration :  {}", url);
    }

    public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = "Подтверждение электронной почты ML-market";
        String senderName = "ML-market";
        String mailContent = "<p>ВНИМАНИЕ, если вы не оставляли заявку на регистрацию на платформе ML-market, то игнорируйте данное сообщение!</p>"+"<p> Здравствуйте, " + theUser.getUsername() + ", </p>" +
                "<p>Благодарим вас за регистрацию в магазине сервисов машинного обучения ML-market.</p>"+
                "<p>Пожалуйста, перейдите по ссылке ниже подтверждения регистрации:</p>" +
                "<a href=\"" + url + "\">Подтвердить электронную почту.</a>" +
                "<p> Надеемся, что сможем помочь вам в поисках необходимого решения. <br> С уважением, команда ML-market.";
        JavaMailSender mailSender = getJavaMailSender();
        MimeMessage message = mailSender.createMimeMessage();
        var messageHelper = new MimeMessageHelper(message,
                StandardCharsets.UTF_8.name());
        messageHelper.setFrom("edmitry.biz@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
