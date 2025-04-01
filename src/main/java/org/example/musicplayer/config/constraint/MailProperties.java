package org.example.musicplayer.config.constraint;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Component
@Getter
public class MailProperties {

    @Value("${spring.mail.username}")
    private String mailFrom;

}
