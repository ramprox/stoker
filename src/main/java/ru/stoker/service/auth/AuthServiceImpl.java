package ru.stoker.service.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stoker.database.entity.User;
import ru.stoker.dto.auth.LoginSuccessDto;
import ru.stoker.dto.auth.RegisterDto;
import ru.stoker.dto.auth.RegisterSuccessDto;
import ru.stoker.dto.profile.CredentialsDto;
import ru.stoker.service.confirmation.ConfirmationService;
import ru.stoker.service.security.JwtService;

@Slf4j
@Service
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;

    private final AuthUserService userService;

    private final ConfirmationService confirmationService;

    @Autowired
    public AuthServiceImpl(JwtService jwtService,
                           AuthUserService userService,
                           ConfirmationService confirmationService) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.confirmationService = confirmationService;
    }

    @Override
    public LoginSuccessDto login(CredentialsDto credentialsDto) {
        String login = credentialsDto.getLogin();
        userService.checkCredentialsAndConfirmed(credentialsDto);
        return new LoginSuccessDto(jwtService.generateToken(login));
    }

    @Override
    @Transactional
    public RegisterSuccessDto register(RegisterDto userDto) {
        User user = userService.register(userDto);
        String responseMessage = confirmationService.startConfirmFlow(user);
        return new RegisterSuccessDto(responseMessage);
    }

    @Override
    public void confirm(Long userId, String code) {
        confirmationService.confirm(userId, code);
    }

}
