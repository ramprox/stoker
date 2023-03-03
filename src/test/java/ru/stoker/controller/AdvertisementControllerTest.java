package ru.stoker.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import ru.stoker.dto.advt.AdvtInfo;
import ru.stoker.dto.advt.CreateAdvt;
import ru.stoker.dto.advt.UpdateAdvt;
import ru.stoker.dto.category.CategoryDto;
import ru.stoker.dto.product.CreateProduct;
import ru.stoker.dto.product.ProductInfo;
import ru.stoker.dto.product.UpdateProduct;
import ru.stoker.dto.product.productproperties.ProductPropertiesDto;
import ru.stoker.dto.profile.AdminUserProfileInfo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.RequestEntity.delete;
import static org.springframework.http.RequestEntity.get;
import static org.springframework.http.RequestEntity.put;
import static ru.stoker.util.factory.AdvtDtosFactory.createAdvtInfo;
import static ru.stoker.util.factory.AdvtDtosFactory.createProductInfo;
import static ru.stoker.util.factory.AdvtDtosFactory.createUpdateAdvt;
import static ru.stoker.util.factory.AdvtDtosFactory.createUpdateProduct;
import static ru.stoker.util.factory.AdvtDtosFactory.getCreateAdvt;
import static ru.stoker.util.factory.AdvtDtosFactory.getCreateProduct;
import static ru.stoker.util.factory.CategoryDtoFactory.categoryDto;

@DisplayName("Интеграционные тесты AdvertisementController")
public class AdvertisementControllerTest extends BaseControllerTest {

    @DisplayName("Сохранение объявление")
    @Test
    public void saveTest() {
        AdminUserProfileInfo savedUser = saveDefaultUserAndGet();
        CategoryDto category = saveCategoryAndGet(categoryDto("Category"));
        ProductPropertiesDto properties = getDefaultNotebookProperties();
        CreateProduct product = getCreateProduct(category.getId(), new BigDecimal("100"), "Description", properties);
        CreateAdvt advt = getCreateAdvt("Name", product);

        ResponseEntity<AdvtInfo> response = saveAdvt(advt, savedUser.getCredentials().getLogin(), AdvtInfo.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        AdvtInfo actualAdvt = response.getBody();
        ProductInfo readProductDto = createProductInfo(product, properties, List.of());
        AdvtInfo expectedAdvt = createAdvtInfo(1L, savedUser.getId(), advt.getName(), readProductDto, LocalDate.now());
        assertThat(actualAdvt).isEqualTo(expectedAdvt);
    }

    @DisplayName("Извлечение объявления по id")
    @Test
    public void getByIdTest() {
        AdminUserProfileInfo user = saveDefaultUserAndGet();
        AdvtInfo expectedAdvt = saveDefaultAdvtAndGet(user.getCredentials().getLogin());

        ResponseEntity<AdvtInfo> response = getById(expectedAdvt.getId(), AdvtInfo.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        AdvtInfo actualAdvt = response.getBody();
        assertThat(actualAdvt).isEqualTo(expectedAdvt);
    }

    @DisplayName("Извлечение объявлений по id категории")
    @Test
    public void findAllTest() {
        AdminUserProfileInfo user = saveDefaultUserAndGet();
        List<AdvtInfo> expectedAdvtInfos = saveDefaultAdvtAndGet(user.getCredentials().getLogin(), 2);

        ResponseEntity<List<AdvtInfo>> response = getAll(1L, new ParameterizedTypeReference<>() { });

        assertThat(response.getStatusCode()).isEqualTo(OK);
        List<AdvtInfo> actualAdvtInfos = response.getBody();
        assertThat(actualAdvtInfos)
                .extracting(AdvtInfo::getId)
                .containsExactlyInAnyOrderElementsOf(expectedAdvtInfos.stream().map(AdvtInfo::getId).toList());
    }

    @DisplayName("Обновление объявления")
    @Test
    public void updateTest() {
        AdminUserProfileInfo user = saveDefaultUserAndGet();
        AdvtInfo expectedAdvt = saveDefaultAdvtAndGet(user.getCredentials().getLogin());
        List<Long> removingAttachments = List.of();
        UpdateProduct updateProduct = createUpdateProduct(expectedAdvt.getProduct(), removingAttachments);
        UpdateAdvt updateAdvt = createUpdateAdvt(expectedAdvt.getId(), "newName", updateProduct);
        expectedAdvt.setName(updateAdvt.getName());
        expectedAdvt.getProduct().getAttachments().removeAll(removingAttachments);
        expectedAdvt.getProduct().setAttachments(List.of());

        ResponseEntity<AdvtInfo> response = updateAdvt(updateAdvt, user.getCredentials().getLogin(), AdvtInfo.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        AdvtInfo actualAdvt = response.getBody();
        assertThat(actualAdvt).isEqualTo(expectedAdvt);
    }

    @DisplayName("Удаление объявления по id")
    @Test
    public void deleteByIdTest() {
        AdminUserProfileInfo user = saveDefaultUserAndGet();
        AdvtInfo advt = saveDefaultAdvtAndGet(user.getCredentials().getLogin());

        ResponseEntity<Void> response = deleteById(advt.getId(), user.getCredentials().getLogin(), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        ResponseEntity<String> responseAfterDelete = getById(advt.getId(), String.class);
        assertThat(responseAfterDelete.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    private <T> ResponseEntity<T> getById(Long id, Class<T> type) {
        return restTemplate.getForEntity("/api/v1/advertisement/{id}", type, id);
    }

    private <T> ResponseEntity<T> getAll(Long categoryId, ParameterizedTypeReference<T> type) {
        Map<String, ?> params = Map.of("categoryId", categoryId);
        String urlTemplate = UriComponentsBuilder.fromUriString("/api/v1/advertisement/all")
                .queryParam("categoryId", categoryId)
                .toUriString();
        HttpEntity<?> entity = new HttpEntity<>(null);
        return restTemplate.exchange(urlTemplate, GET, entity, type, params);
    }

    private <T> ResponseEntity<T> updateAdvt(UpdateAdvt advtDto, String login, Class<T> type) {
        RequestEntity<UpdateAdvt> request = put("/api/v1/advertisement")
                .header(AUTHORIZATION, userAuthHeader(login))
                .body(advtDto);
        return restTemplate.exchange(request, type);
    }

    private <T> ResponseEntity<T> deleteById(Long id, String login, Class<T> type) {
        RequestEntity<Void> request = delete("/api/v1/advertisement/{id}", id)
                .header(AUTHORIZATION, userAuthHeader(login))
                .build();
        return restTemplate.exchange(request, type);
    }

}
