package ru.stoker.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class FavoriteEx {

    @Getter
    @AllArgsConstructor
    public static class NotFoundException extends RuntimeException {

        public static final String USER_FAVORITE_NOT_FOUND = "advt.favorite.not.found";

        public static final String FAVORITE_NOT_FOUND = "favorite.not.found";

        private Long userId;

        private Long advtId;

    }

}
