package ru.stoker.controller.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import ru.stoker.controller.BaseControllerTest;
import ru.stoker.dto.estimation.EstimationDto;
import ru.stoker.dto.profile.AdminUserProfileInfo;
import ru.stoker.dto.userestimation.UpdateUserEstimation;
import ru.stoker.dto.userestimation.UserEstimationDto;
import ru.stoker.dto.userestimation.UserEstimationIdDto;
import ru.stoker.dto.userestimation.UserEstimationInfo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.RequestEntity.put;
import static ru.stoker.util.factory.EstimationDtosFactory.estimationDto;
import static ru.stoker.util.factory.UserEstimationDtosFactory.createUpdateUserEstimation;
import static ru.stoker.util.factory.UserEstimationDtosFactory.createUserEstimationDto;

@DisplayName("Интеграционные тесты AdminUserEstimationController")
public class AdminUserEstimationControllerTest extends BaseControllerTest {

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

    @DisplayName("Обновление оценки пользователя")
    @Test
    public void updateTest() {
        EstimationDto estimationDto = estimationDto(5, "Comment");
        UserEstimationDto userEstimationDto = createUserEstimationDto(user1.getId(), estimationDto);

        UserEstimationInfo expectedUserEstimation = saveEstimation(userEstimationDto,
                user2.getCredentials().getLogin(), UserEstimationInfo.class).getBody();

        UpdateUserEstimation updateDto = createUpdateUserEstimation(user2.getId(), userEstimationDto);
        updateDto.getEstimation().setValue(10);
        expectedUserEstimation.getEstimation().setValue(10);
        ResponseEntity<UserEstimationInfo> response = updateEstimation(updateDto, UserEstimationInfo.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        UserEstimationInfo actualUserEstimation = response.getBody();
        assertThat(actualUserEstimation).isEqualTo(expectedUserEstimation);
    }

    @DisplayName("Удаление оценки пользователя по id оценки")
    @Test
    public void deleteByUserEstimationIdTest() {
        EstimationDto estimationDto = estimationDto(5, "Comment");
        UserEstimationDto userEstimationDto = createUserEstimationDto(user1.getId(), estimationDto);

        saveEstimation(userEstimationDto, user2.getCredentials().getLogin(), UserEstimationInfo.class);

        UserEstimationIdDto dto = new UserEstimationIdDto();
        dto.setUserId(user1.getId());
        dto.setOwnerUserId(user2.getId());
        ResponseEntity<Void> response = deleteByUserEstimationId(dto, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        ResponseEntity<String> responseAfterDelete = getByUserEstimationId(user2.getId(), user1.getId(), String.class);
        assertThat(responseAfterDelete.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    private <T> ResponseEntity<T> updateEstimation(UpdateUserEstimation dto, Class<T> type) {
        RequestEntity<UpdateUserEstimation> request = put("/api/v1/admin/estimation/user")
                .header(AUTHORIZATION, adminAuthHeader())
                .body(dto);
        return restTemplate.exchange(request, type);
    }

    private <T> ResponseEntity<T> deleteByUserEstimationId(UserEstimationIdDto dto, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, adminAuthHeader());
        HttpEntity<UserEstimationIdDto> request = new HttpEntity<>(dto, headers);
        return restTemplate.exchange("/api/v1/admin/estimation/user", DELETE, request, type);
    }

    private <T> ResponseEntity<T> getByUserEstimationId(Long ownerId, Long userId, Class<T> type) {
        return restTemplate.getForEntity("/api/v1/estimation/user/{userId}/{ownerId}", type, userId, ownerId);
    }

}
