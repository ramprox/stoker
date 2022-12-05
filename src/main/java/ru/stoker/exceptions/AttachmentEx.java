package ru.stoker.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AttachmentEx {

    @Getter
    @AllArgsConstructor
    public static class NotFoundException extends RuntimeException {
        public static final String IMAGE_NOT_FOUND = "image.not.found";

        private final Long id;
    }

}
