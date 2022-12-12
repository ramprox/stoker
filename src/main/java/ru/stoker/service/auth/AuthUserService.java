package ru.stoker.service.auth;

import ru.stoker.database.entity.User;
import ru.stoker.dto.auth.RegisterDto;
import ru.stoker.dto.profile.CredentialsDto;

public interface AuthUserService {

    User register(RegisterDto registerDto);

    void checkCredentialsAndConfirmed(CredentialsDto credentialsDto);

}
