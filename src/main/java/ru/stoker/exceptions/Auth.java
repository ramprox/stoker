package ru.stoker.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Auth {

    public static class IncorrectPasswordException extends RuntimeException {
        public static final String PASSWORD_INCORRECT = "password.incorrect";
    }

    @Getter
    @AllArgsConstructor
    public static class LoginNotFoundException extends RuntimeException {
        public static final String LOGIN_NOT_FOUND = "user.login.not.found";

        private final String login;
    }
}
