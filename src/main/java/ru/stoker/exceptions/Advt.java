package ru.stoker.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Advt {
    @Getter
    @AllArgsConstructor
    public static class NotFoundException extends RuntimeException {

        public static String ADVT_NOT_FOUND = "advt.not.found";

        private final Long id;
    }

    public static class SaveOperationException extends RuntimeException {
        public static final String SAVE_OPERATION_FAILED = "save.operation.failed";
    }

    public static class UpdateOperationException extends RuntimeException {
        public static final String UPDATE_OPERATION_FAILED = "update.operation.failed";
    }

    public static class DeleteOperationException extends RuntimeException {
        public static final String DELETE_OPERATION_FAILED = "delete.operation.failed";
    }

}
