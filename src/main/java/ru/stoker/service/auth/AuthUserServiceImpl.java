package ru.stoker.service.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.stoker.database.entity.User;
import ru.stoker.database.repository.UserRepository;
import ru.stoker.dto.auth.RegisterDto;
import ru.stoker.dto.profile.CredentialsDto;
import ru.stoker.dto.profile.ContactsDto;
import ru.stoker.exceptions.Auth.IncorrectPasswordException;
import ru.stoker.exceptions.Auth.LoginNotFoundException;
import ru.stoker.exceptions.ConfirmationEx;
import ru.stoker.mapper.ProfileMapper;
import ru.stoker.service.util.UserChecker;

@Service
public class AuthUserServiceImpl implements AuthUserService {

    private final UserRepository userRepository;

    private final UserChecker userChecker;

    private final PasswordEncoder passwordEncoder;

    private final ProfileMapper profileMapper;

    @Autowired
    public AuthUserServiceImpl(UserRepository userRepository,
                               UserChecker userChecker, PasswordEncoder passwordEncoder,
                               ProfileMapper profileMapper) {
        this.userRepository = userRepository;
        this.userChecker = userChecker;
        this.passwordEncoder = passwordEncoder;
        this.profileMapper = profileMapper;
    }

    @Override
    public User register(RegisterDto registerDto) {
        String login = registerDto.getCredentials().getLogin();
        ContactsDto contactsDto = registerDto.getPersonalData().getContacts();
        userChecker.checkNotExistUser(login, contactsDto);
        User user = profileMapper.fromRegisterDto(registerDto);
        return userRepository.save(user);
    }

    @Override
    public void checkCredentialsAndConfirmed(CredentialsDto credentialsDto) {
        String login = credentialsDto.getLogin();
        String rawPassword = credentialsDto.getPassword();
        User user = getByLogin(login);
        String encodedPassword = user.getCredentials().getPassword();
        checkPasswordMatches(rawPassword, encodedPassword);
        checkStatusConfirmed(user);
    }

    private User getByLogin(String login) {
        return userRepository.findByCredentialsLogin(login)
                .orElseThrow(() -> new LoginNotFoundException(login));
    }

    private void checkPasswordMatches(String rawPassword, String encodedPassword) {
        if(!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new IncorrectPasswordException();
        }
    }

    private void checkStatusConfirmed(User user) {
        if(!user.isConfirmed()) {
            throw new ConfirmationEx.UserNotConfirmedException();
        }
    }

}
