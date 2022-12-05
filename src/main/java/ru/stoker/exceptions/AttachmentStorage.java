package ru.stoker.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.file.Path;

public class AttachmentStorage {

    @Getter
    @AllArgsConstructor
    public static class FileOperationException extends RuntimeException {
        private final Path path;

        public FileOperationException(String message, Path path) {
            super(message);
            this.path = path;
        }
    }

    @Getter
    @AllArgsConstructor
    public static class UnknownImageTypeException extends RuntimeException {
        public static String UNKNOWN_IMAGE_TYPE = "unknown.image.type";

        private final String imageType;
    }
}
