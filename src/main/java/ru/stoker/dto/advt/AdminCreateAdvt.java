package ru.stoker.dto.advt;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.stoker.dto.advt.CreateAdvt;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminCreateAdvt extends CreateAdvt {

    @NotNull(message = "{user.id.not.null}")
    private Long userId;

}
