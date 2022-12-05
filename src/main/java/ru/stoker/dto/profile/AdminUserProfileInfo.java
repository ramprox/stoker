package ru.stoker.dto.profile;

import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.stoker.database.entity.enums.Role;

import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode(callSuper = true)
public class AdminUserProfileInfo extends UserProfileInfo {

    @NotNull(message = "{role.not.null}")
    private Role role;

    private boolean confirmed;

}
