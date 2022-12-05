package ru.stoker.dto.profile;

import lombok.Data;
import ru.stoker.dto.util.validgroups.OnCreate;
import ru.stoker.dto.util.validgroups.OnUpdate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
public class ProfileInfo {

    @Null(groups = OnCreate.class, message = "{user.id.null}")
    @NotNull(groups = OnUpdate.class, message = "{user.id.not.null}")
    private Long id;

    @Valid
    @NotNull(message = "{personalData.not.null}")
    private PersonalDataDto personalData;

}
