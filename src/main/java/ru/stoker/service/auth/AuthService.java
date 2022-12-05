package ru.stoker.service.auth;

import ru.stoker.dto.auth.LoginSuccessDto;
import ru.stoker.dto.auth.RegisterDto;
import ru.stoker.dto.auth.RegisterSuccessDto;
import ru.stoker.dto.profile.CredentialsDto;

public interface AuthService {

    RegisterSuccessDto register(RegisterDto userDto);

    LoginSuccessDto login(CredentialsDto credentials);

    void confirm(Long userId, String code);

}
