package ru.stoker.dto.favorite;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class FavoriteIdDto {

    @NotNull(message = "{user.id.not.null}")
    private Long userId;

    @NotNull(message = "{advt.id.not.null}")
    private Long advtId;

}
