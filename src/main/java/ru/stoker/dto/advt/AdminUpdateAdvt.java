package ru.stoker.dto.advt;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.stoker.dto.advt.UpdateAdvt;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminUpdateAdvt extends UpdateAdvt {

    @NotNull(message = "{user.id.not.null}")
    private Long userId;

}
