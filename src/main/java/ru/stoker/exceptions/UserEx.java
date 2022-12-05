package ru.stoker.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.stoker.model.UserExistErrors;

public class UserEx {

    @Getter
    @AllArgsConstructor
    public static class AlreadyExistException extends RuntimeException {
        private final UserExistErrors errors;
    }

    @Getter
    @AllArgsConstructor
    public static class NotFoundException extends RuntimeException {

        public static final String USER_NOT_FOUND = "user.not.found";

        private final Long id;

    }
}
