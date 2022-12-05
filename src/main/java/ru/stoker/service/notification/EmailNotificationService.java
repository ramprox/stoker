package ru.stoker.service.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import ru.stoker.database.entity.embeddable.Contacts;
import ru.stoker.exceptions.NotifySendException;
import ru.stoker.properties.EmailNotificationProperties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Locale;

@Slf4j
@Service
public class EmailNotificationService implements NotificationService {

    private final JavaMailSender mailSender;

    private final EmailNotificationProperties emailProperties;

    private final TemplateEngine templateEngine;

    private final MessageSource messageSource;

    private static final String SUBJECT = "email.subject";

    private static final String RESPONSE_TEMPLATE = "email.response";

    @Autowired
    public EmailNotificationService(JavaMailSender mailSender,
                                    EmailNotificationProperties emailProperties,
                                    TemplateEngine templateEngine,
                                    MessageSource messageSource) {
        this.mailSender = mailSender;
        this.emailProperties = emailProperties;
        this.templateEngine = templateEngine;
        this.messageSource = messageSource;
    }

    @Override
    public String notify(Contacts contactsDto, String link) {
        Locale locale = LocaleContextHolder.getLocale();
        String htmlBody = getMessage(link, locale);
        String email = contactsDto.getEmail();
        MimeMessage mailMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mailMessage);
        try {
            helper.setTo(email);
            helper.setFrom(emailProperties.getSender());
            helper.setSubject(messageSource.getMessage(SUBJECT, null, locale));
            helper.setText(htmlBody, true);
            mailSender.send(mailMessage);
        } catch (MessagingException | MailException ex) {
            log.error("Ошибка при отправке подтверждения регистрации: {}", ex.getMessage());
            throw new NotifySendException();
        }
        return messageSource.getMessage(RESPONSE_TEMPLATE, new Object[] { email }, locale);
    }

    private String getMessage(String link, Locale locale) {
        Context context = new Context();
        context.setLocale(locale);
        context.setVariable("link", link);
        return templateEngine.process(emailProperties.getEmailTemplateFile(), context);
    }

}
