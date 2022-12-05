package ru.stoker.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ProductEstimationEx {

    @Getter
    @AllArgsConstructor
    public static class UserSelfEstimationException extends RuntimeException {

        public static final String USER_SELF_ESTIMATION = "user.self.product.estimation";

        public static final String USER_SELF_ESTIMATION_UPDATE = "user.self.product.estimation.update";

        private final Long userId;

        private final Long productId;
    }

    @Getter
    @AllArgsConstructor
    public static class AlreadyExistException extends RuntimeException {

        public static final String USER_ALREADY_ESTIMATED = "user.already.product.estimated";

        private final Long userId;

        private final Long productId;
    }

    @Getter
    @AllArgsConstructor
    public static class NotFoundException extends RuntimeException {

        public static final String USER_ESTIMATION_NOT_FOUND = "user.product.estimation.not.found";

        public static final String PRODUCT_ESTIMATION_NOT_FOUND = "product.estimation.not.found";

        private final Long userId;

        private final Long productId;

    }

}
