package ru.stoker.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserEstimationEx {

    @Getter
    @AllArgsConstructor
    public static class SelfEstimationException extends RuntimeException {

        public static final String USER_SELF_ESTIMATION = "user.self.estimation";

        public static final String USER_SELF_ESTIMATION_UPDATE = "user.self.estimation.update";

        private final Long ownerUserId;

        private final Long userId;

    }

    @Getter
    @AllArgsConstructor
    public static class NotFoundException extends RuntimeException {

        public static final String USER_ESTIMATION_NOT_FOUND = "user.estimation.not.found";

        private final Long ownerUserId;

        private final Long userId;

    }

    @Getter
    @AllArgsConstructor
    public static class AlreadyExistException extends RuntimeException {

        public static final String USER_ESTIMATION_EXIST = "user.estimation.exist";

        private final Long ownerUserId;

        private final Long userId;

    }

}
