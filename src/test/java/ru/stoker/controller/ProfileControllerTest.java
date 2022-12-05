package ru.stoker.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import ru.stoker.anotations.ControllerIT;
import ru.stoker.dto.profile.*;
import ru.stoker.model.UserExistErrors;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.RequestEntity.*;
import static ru.stoker.util.builder.CredentialsBuilder.credentials;

@DisplayName("Интеграционные тесты для UserController")
@ControllerIT
public class ProfileControllerTest extends BaseControllerTest {

    @DisplayName("Извлечение пользователя по id")
    @Test
    public void getByIdTest() {
        AdminUserProfileInfo savedUser = saveDefaultUserAndGet();
        ProfileInfo expectedUserDto = new ProfileInfo();
        expectedUserDto.setId(savedUser.getId());
        expectedUserDto.setPersonalData(savedUser.getPersonalData());

        ResponseEntity<ProfileInfo> response = getUserById(2L, ProfileInfo.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        ProfileInfo actualUserDto = response.getBody();
        assertThat(actualUserDto).isEqualTo(expectedUserDto);
    }

    @DisplayName("Извлечение несуществующего пользователя")
    @Test
    public void getNotExistUserTest() {
        ResponseEntity<String> response = getUserById(2L, String.class);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        String actualBody = response.getBody();
        assertThat(actualBody).isEqualTo("Пользователь с id = 2 не найден");
    }

    @DisplayName("Просмотр информации профиля пользователем")
    @Test
    public void getProfileInfoTest() {
        AdminUserProfileInfo savedUser = saveDefaultUserAndGet();
        UserProfileInfo expectedUserProfileInfo = fromAdminUserProfileInfo(savedUser);

        String login = savedUser.getCredentials().getLogin();
        ResponseEntity<UserProfileInfo> response = getProfileInfo(login, UserProfileInfo.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        UserProfileInfo actualUserProfileInfo = response.getBody();
        assertThat(actualUserProfileInfo).isEqualTo(expectedUserProfileInfo);
    }

    @DisplayName("Обновление информации профиля пользователем")
    @Test
    public void updateProfileTest() {
        AdminUserProfileInfo savedUser = saveDefaultUserAndGet();
        PersonalDataDto personalDataDto = savedUser.getPersonalData();

        personalDataDto.getContacts().setEmail("newEmail@mail.ru");
        String login = savedUser.getCredentials().getLogin();
        ResponseEntity<PersonalDataDto> response = updateProfile(personalDataDto, login, PersonalDataDto.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        PersonalDataDto actualUserProfileInfo = response.getBody();
        assertThat(actualUserProfileInfo).isEqualTo(personalDataDto);
    }

    @DisplayName("Обновление логина и пароля пользователем")
    @Test
    public void updateCredentialsTest() {
        AdminUserProfileInfo savedUser = saveDefaultUserAndGet();

        CredentialsDto credentialsDto = credentials()
                .withLogin("newLogin")
                .withPassword("newPassword")
                .buildDto();

        String login = savedUser.getCredentials().getLogin();
        ResponseEntity<CredentialsDto> response = updateCredentials(credentialsDto, login, CredentialsDto.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        CredentialsDto actualBody = response.getBody();
        credentialsDto.setPassword(null);
        assertThat(actualBody).isEqualTo(credentialsDto);
    }

    @DisplayName("Удаление профиля пользователем")
    @Test
    public void deleteByIdTest() {
        AdminUserProfileInfo savedUser = saveDefaultUserAndGet();

        String login = savedUser.getCredentials().getLogin();
        ResponseEntity<Void> response = deleteProfile(login, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        ResponseEntity<String> profileResponse = getProfileInfo(login, String.class);
        assertThat(profileResponse.getStatusCode()).isEqualTo(FORBIDDEN);
    }

    @DisplayName("Удаление профиля пользователем c неправильным token'ом")
    @Test
    public void deleteNotExistUserByIdTest() {
        ResponseEntity<Void> response = deleteProfile("login", Void.class);
        assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
    }

    @DisplayName("Обновление персональных данных с пустым firstName")
    @Test
    public void updateWithIncorrectDataTest() {
        AdminUserProfileInfo savedUser = saveDefaultUserAndGet();
        PersonalDataDto personalData = savedUser.getPersonalData();
        personalData.getFullName().setFirstName("");
        List<String> expectedErrors = List.of("Имя не должно быть пустым");

        String login = savedUser.getCredentials().getLogin();
        ResponseEntity<List<String>> response = updateProfile(personalData, login,
                new ParameterizedTypeReference<>() { });

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        List<String> actualBody = response.getBody();
        assertThat(actualBody).isEqualTo(expectedErrors);
    }

    @DisplayName("Обновление с уже существующим email")
    @Test
    public void saveUsersWithDuplicateLoginTest() {
        AdminUserProfileInfo savedUser = saveDefaultUserAndGet();
        PersonalDataDto personalData = savedUser.getPersonalData();
        personalData.getContacts().setEmail("admin@mail.ru");
        UserExistErrors expectedErrors = new UserExistErrors();
        expectedErrors.setEmail("Пользователь с email 'admin@mail.ru' уже существует");

        String login = savedUser.getCredentials().getLogin();
        ResponseEntity<UserExistErrors> response = updateProfile(personalData, login, UserExistErrors.class);

        assertThat(response.getStatusCode()).isEqualTo(CONFLICT);
        UserExistErrors actualErrors = response.getBody();
        assertThat(actualErrors).isEqualTo(expectedErrors);
    }

    private <T> ResponseEntity<T> getUserById(Long id, Class<T> type) {
        return restTemplate.getForEntity("/api/v1/profile/{id}", type, id);
    }

    private <T> ResponseEntity<T> getProfileInfo(String login, Class<T> type) {
        RequestEntity<Void> request = get("/api/v1/profile")
                .header(AUTHORIZATION, userAuthHeader(login)).build();
        return restTemplate.exchange(request, type);
    }

    private <T> ResponseEntity<T> updateProfile(PersonalDataDto userProfileInfo, String login, Class<T> type) {
        RequestEntity<PersonalDataDto> request = put("/api/v1/profile/personaldata")
                .header(AUTHORIZATION, userAuthHeader(login))
                .body(userProfileInfo);
        return restTemplate.exchange(request, type);
    }

    private <T> ResponseEntity<T> updateProfile(PersonalDataDto userProfileInfo, String login,
                                                ParameterizedTypeReference<T> type) {
        RequestEntity<PersonalDataDto> request = put("/api/v1/profile/personaldata")
                .header(AUTHORIZATION, userAuthHeader(login))
                .body(userProfileInfo);
        return restTemplate.exchange(request, type);
    }

    private <T> ResponseEntity<T> updateCredentials(CredentialsDto credentialsDto, String login, Class<T> type) {
        RequestEntity<CredentialsDto> request = put("/api/v1/profile/credentials")
                .header(AUTHORIZATION, userAuthHeader(login))
                .body(credentialsDto);
        return restTemplate.exchange(request, type);
    }

    private <T> ResponseEntity<T> deleteProfile(String login, Class<T> type) {
        RequestEntity<Void> request = delete("/api/v1/profile")
                .header(AUTHORIZATION, userAuthHeader(login)).build();
        return restTemplate.exchange(request, type);
    }

    public UserProfileInfo fromAdminUserProfileInfo(AdminUserProfileInfo createProfileResp) {
        UserProfileInfo userProfileInfo = new UserProfileInfo();
        userProfileInfo.setId(createProfileResp.getId());
        userProfileInfo.setPersonalData(createProfileResp.getPersonalData());
        userProfileInfo.setCredentials(createProfileResp.getCredentials());
        return userProfileInfo;
    }

}
