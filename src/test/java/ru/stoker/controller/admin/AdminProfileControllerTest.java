package ru.stoker.controller.admin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import ru.stoker.anotations.ControllerIT;
import ru.stoker.controller.BaseControllerTest;
import ru.stoker.dto.profile.AdminUserProfileInfo;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@DisplayName("Интеграционные тесты AdminUserController")
@ControllerIT
public class AdminProfileControllerTest extends BaseControllerTest {

    @DisplayName("Сохранение пользователя")
    @Test
    public void saveTest() {
        AdminUserProfileInfo profileInfo = adminProfileFactory.defaultProfileInfo();

        ResponseEntity<AdminUserProfileInfo> response = saveUser(profileInfo, AdminUserProfileInfo.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        AdminUserProfileInfo actualResponse = response.getBody();
        profileInfo.setId(2L);
        assertThat(actualResponse).isEqualTo(profileInfo);
    }

    @DisplayName("Извлечение информации о профиле по id")
    @Test
    public void getByIdTest() {
        AdminUserProfileInfo savedUser = saveDefaultUserAndGet();

        ResponseEntity<AdminUserProfileInfo> response = getProfileInfo(savedUser.getId(), AdminUserProfileInfo.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        AdminUserProfileInfo actualAdminUserProfileInfo = response.getBody();
        assertThat(actualAdminUserProfileInfo).isEqualTo(savedUser);
    }

    @DisplayName("Извлечение информации о профилях всех пользователей")
    @Test
    public void getAllTest() {
        List<AdminUserProfileInfo> savedUsers = saveDefaultUsersAndGet(3);

        ResponseEntity<List<AdminUserProfileInfo>> response = getProfileInfos(new ParameterizedTypeReference<>() { });

        assertThat(response.getStatusCode()).isEqualTo(OK);
        List<AdminUserProfileInfo> actualProfiles = response.getBody();

        assertThat(actualProfiles).containsSubsequence(savedUsers);
    }

    @DisplayName("Обновление профиля пользователя")
    @Test
    public void updateProfileTest() {
        AdminUserProfileInfo profileInfo = adminProfileFactory.defaultProfileInfo();
        AdminUserProfileInfo saved = saveUserAndGet(profileInfo);
        profileInfo.setId(saved.getId());
        profileInfo.getPersonalData().getContacts().setEmail("newEmail@mail.ru");

        ResponseEntity<AdminUserProfileInfo> response =
                update(profileInfo, AdminUserProfileInfo.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        AdminUserProfileInfo actualProfileInfo = response.getBody();
        assertThat(actualProfileInfo).isEqualTo(profileInfo);
    }

    @DisplayName("Удаление пользователя по id")
    @Test
    public void deleteByIdTest() {
        AdminUserProfileInfo savedUser = saveDefaultUserAndGet();

        ResponseEntity<Void> response = deleteById(savedUser.getId(), Void.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);

        ResponseEntity<String> responseAfterDelete = getProfileInfo(savedUser.getId(), String.class);
        assertThat(responseAfterDelete.getStatusCode()).isEqualTo(NOT_FOUND);
        String actualResponseAfterDelete = responseAfterDelete.getBody();
        assertThat(actualResponseAfterDelete).isEqualTo("Пользователь с id = 2 не найден");
    }

    private List<AdminUserProfileInfo> saveDefaultUsersAndGet(int count) {
        return adminProfileFactory.defaultProfiles(count).stream()
                .map(this::saveUserAndGet)
                .collect(Collectors.toList());
    }

    private <T> ResponseEntity<T> getProfileInfo(Long id, Class<T> type) {
        RequestEntity<Void> request =
                RequestEntity.get("/api/v1/admin/profile/{id}", id)
                        .header(AUTHORIZATION, adminAuthHeader())
                        .build();
        return restTemplate.exchange(request, type);
    }

    private <T> ResponseEntity<T> getProfileInfos(ParameterizedTypeReference<T> type) {
        RequestEntity<Void> request =
                RequestEntity.get("/api/v1/admin/profile/all")
                        .header(AUTHORIZATION, adminAuthHeader())
                        .build();
        return restTemplate.exchange(request, type);
    }

    private <T> ResponseEntity<T> update(AdminUserProfileInfo profileInfo, Class<T> type) {
        RequestEntity<AdminUserProfileInfo> request =
                RequestEntity.put("/api/v1/admin/profile")
                        .header(AUTHORIZATION, adminAuthHeader())
                        .body(profileInfo);
        return restTemplate.exchange(request, type);
    }

    private <T> ResponseEntity<T> deleteById(Long id, Class<T> type) {
        RequestEntity<Void> request = RequestEntity.delete("/api/v1/admin/profile/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, adminAuthHeader())
                .build();
        return restTemplate.exchange(request, type);
    }

}
