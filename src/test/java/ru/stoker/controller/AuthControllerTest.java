package ru.stoker.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import ru.stoker.dto.auth.LoginSuccessDto;
import ru.stoker.dto.auth.RegisterDto;
import ru.stoker.dto.auth.RegisterSuccessDto;
import ru.stoker.dto.profile.AdminUserProfileInfo;
import ru.stoker.dto.profile.CredentialsDto;
import ru.stoker.dto.profile.PersonalDataDto;
import ru.stoker.exceptions.NotifySendException;
import ru.stoker.service.notification.NotificationService;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.OK;
import static ru.stoker.util.builder.CredentialsBuilder.credentials;
import static ru.stoker.util.builder.PersonalDataBuilder.personalData;

@DisplayName("Интеграционные тесты AuthController")
public class AuthControllerTest extends BaseControllerTest {

    @MockBean
    private NotificationService notificationService;

    @Captor
    ArgumentCaptor<String> linkCapture;

    @DisplayName("Регистрация пользователя")
    @Test
    public void registerTest() throws NotifySendException {
        RegisterDto registerDto = new RegisterDto();
        CredentialsDto credentialsDto = credentials().buildDto();
        PersonalDataDto personalDataDto = personalData().buildDto();
        registerDto.setPersonalData(personalDataDto);
        registerDto.setCredentials(credentialsDto);
        String expectedMessage = """
                Мы отправили на Вашу почту 'email@mail.ru' письмо.\\n\\
                  Пожалуйста, откройте его и пройдите по ссылке с кодом для подтверждения регистрации.
                """;
        when(notificationService.notify(any(), linkCapture.capture()))
                .thenReturn(expectedMessage);

        ResponseEntity<RegisterSuccessDto> response = register(registerDto, RegisterSuccessDto.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        RegisterSuccessDto actualBody = response.getBody();
        RegisterSuccessDto expectedBody = new RegisterSuccessDto(expectedMessage);
        assertThat(actualBody).isEqualTo(expectedBody);

        String link = linkCapture.getValue();
        link = link.substring("http://localhost:8080".length());

        ResponseEntity<String> confirmResponse = confirm(link, String.class);

        assertThat(confirmResponse.getStatusCode()).isEqualTo(OK);
        String expectedSuccessConfirmMsg = "Вы успешно подтвердили регистрацию. Пожайлуста, авторизуйтесь.";
        String actualSuccessConfirmMsg = confirmResponse.getBody();
        assertThat(actualSuccessConfirmMsg).isEqualTo(expectedSuccessConfirmMsg);
    }

    @DisplayName("Аутентификация пользователя")
    @Test
    public void loginTest() {
        AdminUserProfileInfo createProfileReq = adminProfileFactory.defaultProfileInfo();
        saveUserAndGet(createProfileReq);
        CredentialsDto credentialsDto = createProfileReq.getCredentials();
        LoginSuccessDto expectedResponse = new LoginSuccessDto();
        expectedResponse.setToken(token(credentialsDto.getLogin()));

        ResponseEntity<LoginSuccessDto> response = login(credentialsDto, LoginSuccessDto.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        LoginSuccessDto actualResponse = response.getBody();
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    private <T> ResponseEntity<T> register(RegisterDto registerDto, Class<T> type) {
        return restTemplate.postForEntity("/api/v1/auth/register", registerDto, type);
    }

    private <T> ResponseEntity<T> login(CredentialsDto credentialsDto, Class<T> type) {
        return restTemplate.postForEntity("/api/v1/auth/login", credentialsDto, type);
    }

    private <T> ResponseEntity<T> confirm(String link, Class<T> type) {
        return restTemplate.getForEntity(link, type);
    }

}
