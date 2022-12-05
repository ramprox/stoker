package ru.stoker.service.exceptions;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import ru.stoker.model.UserExistErrors;

import java.util.Locale;

import static ru.stoker.model.UserExistErrors.*;

@Component
public class UserExistErrorsBuilder {

    private final MessageSource messageSource;

    public UserExistErrorsBuilder(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public UserExistErrors buildErrors(UserExistErrors errors, Locale locale) {
        if(errors.getLogin() != null) {
            String loginMessage = messageSource.getMessage(LOGIN_EXIST, new Object[]{errors.getLogin()}, locale);
            errors.setLogin(loginMessage);
        }
        if(errors.getEmail() != null) {
            String emailMessage = messageSource.getMessage(EMAIL_EXIST, new Object[]{errors.getEmail()}, locale);
            errors.setEmail(emailMessage);
        }
        if(errors.getPhone() != null) {
            String phoneMessage = messageSource.getMessage(PHONE_EXIST, new Object[]{errors.getPhone()}, locale);
            errors.setPhone(phoneMessage);
        }
        return errors;
    }

}
