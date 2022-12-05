package ru.stoker.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ProductEx {

    @Getter
    @AllArgsConstructor
    public static class NotFoundException extends RuntimeException {
        private final Long id;
    }

}
