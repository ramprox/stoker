package ru.stoker.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import ru.stoker.dto.advt.AdvtInfo;
import ru.stoker.dto.profile.AdminUserProfileInfo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.RequestEntity.delete;
import static org.springframework.http.RequestEntity.get;

@DisplayName("Интеграционные тесты FavoriteController")
public class FavoriteControllerTest extends BaseControllerTest {

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

    @DisplayName("Сохранение пользователем объявления в избранном")
    @Test
    public void saveTest() {
        AdvtInfo advtDto = saveDefaultAdvtAndGet(user1.getCredentials().getLogin());

        ResponseEntity<Void> response = saveFavorite(advtDto.getId(), user2.getCredentials().getLogin(), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    @DisplayName("Извлечение пользователем объявлений из избранного")
    @Test
    public void getAllTest() {
        AdvtInfo advtDto = saveDefaultAdvtAndGet(user1.getCredentials().getLogin());
        String user2Login = user2.getCredentials().getLogin();
        saveFavorite(advtDto.getId(), user2Login, Void.class);

        ResponseEntity<List<AdvtInfo>> response = getAll(user2Login, new ParameterizedTypeReference<>() { });

        assertThat(response.getStatusCode()).isEqualTo(OK);
        List<AdvtInfo> actualFavorites = response.getBody();
        List<AdvtInfo> expectedFavorites = List.of(advtDto);
        assertThat(actualFavorites).isEqualTo(expectedFavorites);
    }

    @DisplayName("Удаление пользователем объявления из избранного по его id")
    @Test
    public void deleteByAdvtIdTest() {
        AdvtInfo advtDto = saveDefaultAdvtAndGet(user1.getCredentials().getLogin());
        String user2Login = user2.getCredentials().getLogin();
        saveFavorite(advtDto.getId(), user2.getCredentials().getLogin(), Void.class);

        ResponseEntity<Void> response = deleteByAdvtId(advtDto.getId(), user2Login, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        ResponseEntity<List<AdvtInfo>> responseList = getAll(user2Login, new ParameterizedTypeReference<>() { });
        List<AdvtInfo> actualFavorites = responseList.getBody();
        assertThat(actualFavorites.isEmpty()).isEqualTo(true);
    }

    @DisplayName("Повторное добавление в избранное")
    @Test
    public void repeatAddToFavoritesTest() {
        AdvtInfo advtInfo = saveDefaultAdvtAndGet(user1.getCredentials().getLogin());
        String user2Login = user2.getCredentials().getLogin();
        saveFavorite(advtInfo.getId(), user2Login, Void.class);

        ResponseEntity<Void> response = saveFavorite(advtInfo.getId(), user2Login, Void.class);

    }

    @DisplayName("Добавление несуществующего объявления в избранное")
    @Test
    public void addNotExistingAdvtTest() {
        ResponseEntity<String> response = saveFavorite(1L, user2.getCredentials().getLogin(), String.class);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        String errorMessage = response.getBody();
        assertThat(errorMessage).isEqualTo("Объявление с id = 1 не найдено");
    }

    @DisplayName("Удаление несуществующего объявления из избранного")
    @Test
    public void deleteOtherAdvtTest() {
        String user2Login = user2.getCredentials().getLogin();

        ResponseEntity<String> response = deleteByAdvtId(1L, user2Login, String.class);

        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
        String errorMessage = response.getBody();
        assertThat(errorMessage).isEqualTo("В избранных нет объявления с id = 1");
    }

    private <T> ResponseEntity<T> getAll(String login, ParameterizedTypeReference<T> type) {
        RequestEntity<Void> request = get("/api/v1/favorite/all")
                .header(AUTHORIZATION, userAuthHeader(login))
                .build();
        return restTemplate.exchange(request, type);
    }

    private <T> ResponseEntity<T> deleteByAdvtId(Long advtId, String login, Class<T> type) {
        RequestEntity<Void> request = delete("/api/v1/favorite/{advtId}", advtId)
                .header(AUTHORIZATION, userAuthHeader(login))
                .build();
        return restTemplate.exchange(request, type);
    }

}
