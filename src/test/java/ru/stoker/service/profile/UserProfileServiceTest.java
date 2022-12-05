package ru.stoker.service.profile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import ru.stoker.database.entity.User;
import ru.stoker.database.repository.UserRepository;
import ru.stoker.dto.profile.CredentialsDto;
import ru.stoker.dto.profile.PersonalDataDto;
import ru.stoker.dto.profile.ProfileInfo;
import ru.stoker.dto.profile.UserProfileInfo;
import ru.stoker.mapper.CredentialMapper;
import ru.stoker.mapper.CredentialMapperImpl;
import ru.stoker.mapper.ProfileMapper;
import ru.stoker.mapper.ProfileMapperImpl;
import ru.stoker.service.util.UserChecker;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static ru.stoker.util.builder.UserBuilder.user;

@DisplayName("Unit тесты UserProfileService")
@ExtendWith(MockitoExtension.class)
public class UserProfileServiceTest {

    private UserProfileService userProfileService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserChecker userChecker;

    private ProfileMapper profileMapper;

    @BeforeEach
    public void beforeEach() {
        CredentialMapper credentialMapper = new CredentialMapperImpl();
        ReflectionTestUtils.setField(credentialMapper, "passwordEncoder", new BCryptPasswordEncoder());
        profileMapper = new ProfileMapperImpl(credentialMapper);
        userProfileService = new UserProfileServiceImpl(userRepository, userChecker, profileMapper);
    }

    @DisplayName("Извлечение информации о пользователе по id")
    @Test
    public void findByIdTest() {
        User user = user().withId(1L).build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        ProfileInfo actualProfileInfo = userProfileService.findById(1L);

        ProfileInfo expectedProfileInfo = profileMapper.toProfileInfo(user);
        assertThat(actualProfileInfo).isEqualTo(expectedProfileInfo);
    }

    @DisplayName("Извлечение пользователем информации о профиле по id")
    @Test
    public void getByIdTest() {
        User user = user().withId(1L).build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserProfileInfo actualProfileInfo = userProfileService.getById(1L);

        UserProfileInfo expectedProfileInfo = profileMapper.toUserProfileInfo(user);
        assertThat(actualProfileInfo).isEqualTo(expectedProfileInfo);
    }

    @DisplayName("Обновление персональных данных")
    @Test
    public void updatePersonalDataTest() {
        User user = user().withId(1L).build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        PersonalDataDto personalDataDto = profileMapper.toProfileInfo(user).getPersonalData();
        personalDataDto.getContacts().setEmail("newEmail@mail.ru");

        PersonalDataDto actualPersonalData = userProfileService.updatePersonalData(1L, personalDataDto);

        assertThat(actualPersonalData).isEqualTo(personalDataDto);
    }

    @DisplayName("Обновление логина и пароля")
    @Test
    public void updateCredentialsTest() {
        User user = user().withId(1L).build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        CredentialsDto credentialsDto = profileMapper.toUserProfileInfo(user).getCredentials();
        credentialsDto.setLogin("newLogin");
        credentialsDto.setPassword("password");

        CredentialsDto actualCredentials = userProfileService.updateCredentials(1L, credentialsDto);

        assertThat(actualCredentials).isEqualTo(credentialsDto);
    }

    @DisplayName("Удаление по id")
    @Test
    public void deleteByIdTest() {
        User user = user().withId(1L).build();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userProfileService.deleteById(1L);

        verify(userRepository).delete(user);
    }

}
