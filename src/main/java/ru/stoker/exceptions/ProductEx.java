package ru.stoker.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ProductEx {

    @Getter
    @AllArgsConstructor
    public static class NotFoundException extends RuntimeException {

        public static final String PRODUCT_NOT_FOUND = "product.not.found";

        private final Long id;

    }

}
