package ru.stoker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.*;
import ru.stoker.dto.auth.LoginSuccessDto;
import ru.stoker.dto.auth.RegisterDto;
import ru.stoker.dto.auth.RegisterSuccessDto;
import ru.stoker.dto.profile.CredentialsDto;
import ru.stoker.exceptions.Auth;
import ru.stoker.exceptions.ConfirmationEx;
import ru.stoker.exceptions.NotifySendException;
import ru.stoker.exceptions.UserEx;
import ru.stoker.model.UserExistErrors;
import ru.stoker.service.auth.AuthService;
import ru.stoker.service.exceptions.UserExistErrorsBuilder;

import javax.validation.Valid;
import java.util.Locale;

import static org.springframework.http.HttpStatus.*;
import static ru.stoker.exceptions.Auth.IncorrectPasswordException.PASSWORD_INCORRECT;
import static ru.stoker.exceptions.Auth.LoginNotFoundException.LOGIN_NOT_FOUND;
import static ru.stoker.exceptions.ConfirmationEx.ConfirmTimeExpiredException.CONFIRM_TIME_EXPIRED;
import static ru.stoker.exceptions.ConfirmationEx.UserAlreadyConfirmedException.USER_ALREADY_CONFIRMED;
import static ru.stoker.exceptions.ConfirmationEx.UserNotConfirmedException.USER_NOT_CONFIRMED;
import static ru.stoker.exceptions.NotifySendException.CONFIRM_EXCEPTION_MESSAGE;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    private final UserExistErrorsBuilder builder;

    private final MessageSource messageSource;

    private static final String SUCCESS_CONFIRM = "success.confirm";

    @Autowired
    public AuthController(AuthService authService,
                          UserExistErrorsBuilder builder,
                          MessageSource messageSource) {
        this.authService = authService;
        this.builder = builder;
        this.messageSource = messageSource;
    }

    @PostMapping("/register")
    public RegisterSuccessDto register(@RequestBody @Valid RegisterDto userDto) {
        return authService.register(userDto);
    }

    @PostMapping("/login")
    public LoginSuccessDto login(@RequestBody @Valid CredentialsDto credentials) {
        return authService.login(credentials);
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam Long userId, @RequestParam String code, Locale locale) {
        authService.confirm(userId, code);
        return messageSource.getMessage(SUCCESS_CONFIRM, null, locale);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(Auth.IncorrectPasswordException.class)
    public String handleIncorrectPasswordException(Locale locale) {
        return messageSource.getMessage(PASSWORD_INCORRECT, null, locale);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ConfirmationEx.ConfirmTimeExpiredException.class)
    public String handleConfirmTimeExpiredException(Locale locale) {
        return messageSource.getMessage(CONFIRM_TIME_EXPIRED, null, locale);
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(ConfirmationEx.UserAlreadyConfirmedException.class)
    public String handleUserAlreadyConfirmedException(Locale locale) {
        return messageSource.getMessage(USER_ALREADY_CONFIRMED, null, locale);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(ConfirmationEx.UserNotConfirmedException.class)
    public String handleUserNotConfirmedException(Locale locale) {
        return messageSource.getMessage(USER_NOT_CONFIRMED, null, locale);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(Auth.LoginNotFoundException.class)
    public String handleLoginNotFoundException(Auth.LoginNotFoundException ex, Locale locale) {
        return messageSource.getMessage(LOGIN_NOT_FOUND, new Object[]{ ex.getLogin() }, locale);
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(UserEx.AlreadyExistException.class)
    public UserExistErrors handleUserAlreadyExistException(UserEx.AlreadyExistException ex, Locale locale) {
        return builder.buildErrors(ex.getErrors(), locale);
    }

    @ResponseStatus(SERVICE_UNAVAILABLE)
    @ExceptionHandler(NotifySendException.class)
    public String handleNotifySendException(Locale locale) {
        return messageSource.getMessage(CONFIRM_EXCEPTION_MESSAGE, null, locale);
    }

}
