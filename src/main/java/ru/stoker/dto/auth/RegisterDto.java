package ru.stoker.dto.auth;

import lombok.Data;
import ru.stoker.dto.profile.CredentialsDto;
import ru.stoker.dto.profile.PersonalDataDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class RegisterDto {

    @Valid
    @NotNull(message = "{credentials.not.null}")
    private CredentialsDto credentials;

    @Valid
    @NotNull(message = "{personalData.not.null}")
    private PersonalDataDto personalData;

}
