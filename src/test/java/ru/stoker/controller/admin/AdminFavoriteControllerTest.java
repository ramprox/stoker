package ru.stoker.controller.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import ru.stoker.controller.BaseControllerTest;
import ru.stoker.dto.advt.AdvtInfo;
import ru.stoker.dto.favorite.FavoriteIdDto;
import ru.stoker.dto.profile.AdminUserProfileInfo;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.OK;

@DisplayName("Интеграционные тесты для AdminFavoriteController")
public class AdminFavoriteControllerTest extends BaseControllerTest {

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

    @DisplayName("Извлечение объявлений пользователя по его id")
    @Test
    public void getByUserIdTest() {
        AdvtInfo advtDto = saveDefaultAdvtAndGet(user1.getCredentials().getLogin());
        saveFavorite(advtDto.getId(), user2.getCredentials().getLogin(), Void.class);

        ResponseEntity<List<AdvtInfo>> response = getAllByUserId(user2.getId(),
                new ParameterizedTypeReference<>() { });

        assertThat(response.getStatusCode()).isEqualTo(OK);
        List<AdvtInfo> actualAdvtInfos = response.getBody();
        List<AdvtInfo> expectedAdvtInfos = List.of(advtDto);
        assertThat(actualAdvtInfos).isEqualTo(expectedAdvtInfos);
    }

    @DisplayName("Удаление по id пользователя и id объявления")
    @Test
    public void deleteByFavoriteIdTest() {
        AdvtInfo advtDto = saveDefaultAdvtAndGet(user1.getCredentials().getLogin());
        saveFavorite(advtDto.getId(), user2.getCredentials().getLogin(), Void.class);

        FavoriteIdDto favoriteIdDto = new FavoriteIdDto();
        favoriteIdDto.setUserId(user2.getId());
        favoriteIdDto.setAdvtId(advtDto.getId());
        ResponseEntity<Void> response = deleteByFavoriteId(favoriteIdDto, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        List<AdvtInfo> actualAdvtInfos = getAllByUserId(user2.getId(),
                new ParameterizedTypeReference<List<AdvtInfo>>() { }).getBody();
        assertThat(actualAdvtInfos.isEmpty()).isEqualTo(true);
    }

    private <T> ResponseEntity<T> getAllByUserId(Long userId, ParameterizedTypeReference<T> type) {
        Map<String, ?> params = Map.of("userId", userId);
        String urlTemplate = UriComponentsBuilder.fromUriString("/api/v1/admin/favorite/all")
                .queryParam("userId", userId)
                .toUriString();
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, adminAuthHeader());
        HttpEntity<?> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(urlTemplate, GET, entity, type, params);
    }

    private <T> ResponseEntity<T> deleteByFavoriteId(FavoriteIdDto favoriteIdDto, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, adminAuthHeader());
        HttpEntity<FavoriteIdDto> request = new HttpEntity<>(favoriteIdDto, headers);
        return restTemplate.exchange("/api/v1/admin/favorite", DELETE, request, type);
    }

}
