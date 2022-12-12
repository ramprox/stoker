package ru.stoker.exceptions;

public class ConfirmationEx {
    public static class ConfirmTimeExpiredException extends RuntimeException {
        public static final String CONFIRM_TIME_EXPIRED = "confirm.time.expired";
    }

    public static class UserAlreadyConfirmedException extends RuntimeException {
        public static final String USER_ALREADY_CONFIRMED = "user.already.confirmed";
    }

    public static class UserNotConfirmedException extends RuntimeException {
        public static final String USER_NOT_CONFIRMED = "user.not.confirmed";
    }

    public static class ConfirmCodeIncorrectException extends RuntimeException {
        public static final String CONFIRM_CODE_INCORRECT = "confirm.code.incorrect";
    }

}
