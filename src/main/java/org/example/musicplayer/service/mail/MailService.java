package org.example.musicplayer.service.mail;

import lombok.extern.slf4j.Slf4j;
import org.example.musicplayer.config.constraint.MailProperties;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class MailService {

    private final JavaMailSender javaMailSender;
    private final MailProperties mailProperties;

    public MailService(final JavaMailSender javaMailSender,
                       final MailProperties mailProperties) {
        this.javaMailSender = javaMailSender;
        this.mailProperties = mailProperties;
    }

    @Async
    public void sendMail(final String mailTo, final String subject, final String html) {
        log.info("sending mail {} to {}", subject, mailTo);

        javaMailSender.send(mimeMessage -> {
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom(mailProperties.getMailFrom());
            message.setTo(mailTo);
            message.setSubject(subject);
            message.setText(html, true);
        });

        log.info("sending completed");
    }


    public void sendMailAuthenticated(final String mailTo,
                                      final String subject,
                                      final String html) {
        log.info("sending mail {} to {}", subject, mailTo);

        javaMailSender.send(mimeMessage -> {
            final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            message.setFrom(mailProperties.getMailFrom());
            message.setTo(mailTo);
            message.setSubject(subject);
            message.setText(html, true);
        });
        log.info("sending completed");
    }
}
