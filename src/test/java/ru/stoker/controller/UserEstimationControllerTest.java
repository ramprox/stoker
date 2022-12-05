package ru.stoker.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import ru.stoker.dto.estimation.EstimationDto;
import ru.stoker.dto.estimation.EstimationInfo;
import ru.stoker.dto.profile.AdminUserProfileInfo;
import ru.stoker.dto.userestimation.UserEstimationDto;
import ru.stoker.dto.userestimation.UserEstimationInfo;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.RequestEntity.delete;
import static org.springframework.http.RequestEntity.put;
import static ru.stoker.util.factory.EstimationDtosFactory.estimationDto;
import static ru.stoker.util.factory.EstimationDtosFactory.estimationInfo;
import static ru.stoker.util.factory.UserEstimationDtosFactory.createReadUserEstimationDto;
import static ru.stoker.util.factory.UserEstimationDtosFactory.createUserEstimationDto;

@DisplayName("Интеграционные тесты UserEstimationController")
public class UserEstimationControllerTest extends BaseControllerTest {

    protected AdminUserProfileInfo user1;

    protected AdminUserProfileInfo user2;

    @Override
    @BeforeEach
    public void beforeEach() {
        super.beforeEach();
        List<AdminUserProfileInfo> adminUserProfileInfos = saveDefaultUsers(2);
        user1 = adminUserProfileInfos.get(0);
        user2 = adminUserProfileInfos.get(1);
    }

    @DisplayName("Оценивание пользователя")
    @Test
    public void estimateTest() {
        EstimationDto estimationDto = estimationDto(5, "Comment");
        UserEstimationDto userEstimationDto = createUserEstimationDto(user1.getId(), estimationDto);

        ResponseEntity<UserEstimationInfo> response = saveEstimation(userEstimationDto, user2.getCredentials().getLogin(),
                UserEstimationInfo.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        UserEstimationInfo actualUserEstimation = response.getBody();
        EstimationInfo expectedReadEstimationDto = estimationInfo(estimationDto, null);
        UserEstimationInfo expectedUserEstimation = createReadUserEstimationDto(user2.getId(), user1.getId(), expectedReadEstimationDto);
        assertThat(actualUserEstimation).isEqualTo(expectedUserEstimation);
    }

    @DisplayName("Обновление оценки пользователя")
    @Test
    public void updateTest() throws Exception {
        EstimationDto estimationDto = estimationDto(5, "Comment");
        UserEstimationDto userEstimationDto = createUserEstimationDto(user1.getId(), estimationDto);
        UserEstimationInfo expectedUserEstimation = saveEstimation(userEstimationDto,
                user2.getCredentials().getLogin(), UserEstimationInfo.class).getBody();

        userEstimationDto.getEstimation().setValue(10);
        expectedUserEstimation.getEstimation().setValue(10);
        ResponseEntity<UserEstimationInfo> response = updateEstimation(userEstimationDto,
                user2.getCredentials().getLogin(), UserEstimationInfo.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        UserEstimationInfo actualUserEstimation = response.getBody();
        assertThat(actualUserEstimation).isEqualTo(expectedUserEstimation);
    }

    @DisplayName("Извлечение всех оценок пользователя по его id")
    @Test
    public void getAllByUserIdTest() throws Exception {
        EstimationDto estimationDto = estimationDto(5, "Comment");
        UserEstimationDto userEstimationDto = createUserEstimationDto(user1.getId(), estimationDto);
        UserEstimationInfo expectedUserEstimation = saveEstimation(userEstimationDto,
                user2.getCredentials().getLogin(), UserEstimationInfo.class).getBody();

        ResponseEntity<List<UserEstimationInfo>> response = getAllByUserId(user1.getId(),
                new ParameterizedTypeReference<>() { });

        assertThat(response.getStatusCode()).isEqualTo(OK);
        List<UserEstimationInfo> actualEstimations = response.getBody();
        List<UserEstimationInfo> expectedEstimations = List.of(expectedUserEstimation);
        assertThat(actualEstimations).containsExactlyInAnyOrderElementsOf(expectedEstimations);
    }

    @DisplayName("Удаление оценки пользователя по его id")
    @Test
    public void deleteByUserIdTest() throws Exception {
        EstimationDto estimationDto = estimationDto(5, "Comment");
        UserEstimationDto userEstimationDto = createUserEstimationDto(user1.getId(), estimationDto);
        saveEstimation(userEstimationDto, user2.getCredentials().getLogin(), UserEstimationInfo.class);

        ResponseEntity<Void> response = deleteByUserId(user1.getId(), user2.getCredentials().getLogin(), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        List<UserEstimationInfo> actualEstimations = getAllByUserId(user1.getId(),
                new ParameterizedTypeReference<List<UserEstimationInfo>>() { }).getBody();
        assertThat(actualEstimations.isEmpty()).isEqualTo(true);
    }

    private <T> ResponseEntity<T> updateEstimation(UserEstimationDto userEstimationDto,
                                           String login, Class<T> type) {
        RequestEntity<UserEstimationDto> request = put("/api/v1/estimation/user")
                .header(AUTHORIZATION, userAuthHeader(login))
                .body(userEstimationDto);
        return restTemplate.exchange(request, type);
    }

    private <T> ResponseEntity<T> getAllByUserId(Long userId, ParameterizedTypeReference<T> type) throws Exception {
        Map<String, ?> params = Map.of("userId", userId);
        String urlTemplate = UriComponentsBuilder.fromUriString("/api/v1/estimation/user")
                .queryParam("userId", userId)
                .toUriString();
        HttpEntity<?> entity = new HttpEntity<>(null);
        return restTemplate.exchange(urlTemplate, GET, entity, type, params);
    }

    private <T> ResponseEntity<T> deleteByUserId(Long userId, String login, Class<T> type) {
        RequestEntity<Void> request = delete("/api/v1/estimation/user/{userId}", userId)
                .header(AUTHORIZATION, userAuthHeader(login))
                .build();
        return restTemplate.exchange(request, type);
    }

}
