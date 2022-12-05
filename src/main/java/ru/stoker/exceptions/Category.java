package ru.stoker.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Category {

    @Getter
    @AllArgsConstructor
    public static class AlreadyExistException extends RuntimeException {

        public static final String CATEGORY_ALREADY_EXIST = "category.already.exist";

        private final Long id;

        private final String name;

    }

    @Getter
    @AllArgsConstructor
    public static class NotFoundException extends RuntimeException {

        public static String CATEGORY_NOT_FOUND = "category.not.found";

        private final Long id;

    }

    @Getter
    @AllArgsConstructor
    public static class ParentNotFoundException extends RuntimeException {

        public static final String PARENT_CATEGORY_NOT_FOUND = "parent.category.not.found";

        private final Long id;

    }

    @Getter
    @AllArgsConstructor
    public static class RootAlreadyExistException extends RuntimeException {

        public static final String ROOT_CATEGORY_EXIST = "root.category.already.exist";

        private final String name;

    }

}
