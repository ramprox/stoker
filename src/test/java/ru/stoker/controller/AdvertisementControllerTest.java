package ru.stoker.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.http.RequestEntity.*;
import static ru.stoker.util.factory.ByteArrayResourceFactory.createByteArrayResource;
import static ru.stoker.util.factory.CategoryDtoFactory.categoryDto;
import static ru.stoker.util.factory.AdvtDtosFactory.*;

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
        List<byte[]> files = List.of(
                randomBytes(10),
                randomBytes(20)
        );

        ResponseEntity<AdvtInfo> response = saveAdvt(advt, files, savedUser.getCredentials().getLogin(), AdvtInfo.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        AdvtInfo actualAdvt = response.getBody();
        ProductInfo readProductDto = createProductInfo(product, properties, List.of(1L, 2L));
        AdvtInfo expectedAdvt = createAdvtInfo(1L, savedUser.getId(), advt.getName(), readProductDto, LocalDate.now());
        assertThat(actualAdvt).isEqualTo(expectedAdvt);
        List<Long> attIds = readProductDto.getAttachments();
        for(int i = 0; i < attIds.size(); i++) {
            ResponseEntity<byte[]> responseAttach = getAttachment(attIds.get(i), byte[].class);
            assertThat(responseAttach.getStatusCode()).isEqualTo(OK);
            assertThat(responseAttach.getHeaders().get(CONTENT_TYPE)).contains(IMAGE_JPEG_VALUE);
            byte[] actualContent = responseAttach.getBody();
            byte[] expectedContent = files.get(i);
            assertThat(actualContent).isEqualTo(expectedContent);
        }
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
        List<Long> removingAttachments = List.of(1L, 2L);
        UpdateProduct updateProduct = createUpdateProduct(expectedAdvt.getProduct(), removingAttachments);
        UpdateAdvt updateAdvt = createUpdateAdvt(expectedAdvt.getId(), "newName", updateProduct);
        expectedAdvt.setName(updateAdvt.getName());
        expectedAdvt.getProduct().getAttachments().removeAll(removingAttachments);
        expectedAdvt.getProduct().setAttachments(List.of(3L));

        ResponseEntity<AdvtInfo> response = updateAdvt(updateAdvt,
                List.of(randomBytes(20)), user.getCredentials().getLogin(), AdvtInfo.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        AdvtInfo actualAdvt = response.getBody();
        assertThat(actualAdvt).isEqualTo(expectedAdvt);

        for (Long attId : removingAttachments) {
            ResponseEntity<String> attResponse = getAttachment(attId, String.class);
            assertThat(attResponse.getStatusCode()).isEqualTo(NOT_FOUND);
        }

        ResponseEntity<byte[]> attachment = getAttachment(3L, byte[].class);
        assertThat(attachment.getStatusCode()).isEqualTo(OK);
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

    private <T> ResponseEntity<T> updateAdvt(UpdateAdvt advtDto, List<byte[]> files, String login, Class<T> type) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        files.forEach(file -> {
            ByteArrayResource resource = createByteArrayResource(file);
            HttpHeaders fileHeaders = new HttpHeaders();
            fileHeaders.add(CONTENT_TYPE, IMAGE_JPEG_VALUE);
            HttpEntity<ByteArrayResource> entity = new HttpEntity<>(resource, fileHeaders);
            body.add("files", entity);
        });
        body.add("advertisement", advtDto);
        RequestEntity<MultiValueMap<String, Object>> request = put("/api/v1/advertisement")
                .header(AUTHORIZATION, userAuthHeader(login))
                .contentType(MULTIPART_FORM_DATA)
                .body(body);
        return restTemplate.exchange(request, type);
    }

    private <T> ResponseEntity<T> deleteById(Long id, String login, Class<T> type) {
        RequestEntity<Void> request = delete("/api/v1/advertisement/{id}", id)
                .header(AUTHORIZATION, userAuthHeader(login))
                .build();
        return restTemplate.exchange(request, type);
    }

    private <T> ResponseEntity<T> getAttachment(Long id, Class<T> type) {
        RequestEntity<Void> request = get("/api/v1/attachment/{id}", id).build();
        return restTemplate.exchange(request, type);
    }

}
