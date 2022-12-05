package ru.stoker.controller.admin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.stoker.controller.BaseControllerTest;
import ru.stoker.dto.advt.AdminCreateAdvt;
import ru.stoker.dto.advt.AdminUpdateAdvt;
import ru.stoker.dto.advt.AdvtInfo;
import ru.stoker.dto.category.CategoryDto;
import ru.stoker.dto.product.CreateProduct;
import ru.stoker.dto.product.UpdateProduct;
import ru.stoker.dto.product.productproperties.notebook.NotebookPropertiesDto;
import ru.stoker.dto.profile.AdminUserProfileInfo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.http.RequestEntity.*;
import static ru.stoker.util.factory.AdvtDtosFactory.*;
import static ru.stoker.util.factory.ByteArrayResourceFactory.createByteArrayResource;
import static ru.stoker.util.factory.CategoryDtoFactory.categoryDto;

@DisplayName("Интеграционные тесты AdminAdvtController")
public class AdminAdvtControllerTest extends BaseControllerTest {

    @DisplayName("Создание объявления")
    @Test
    public void createTest() {
        AdminUserProfileInfo user = saveDefaultUserAndGet();
        CategoryDto categoryDto = saveCategoryAndGet(categoryDto("Category"));
        NotebookPropertiesDto notebookProperties = getDefaultNotebookProperties();
        CreateProduct product = getCreateProduct(categoryDto.getId(), new BigDecimal("1000"),
                "Description", notebookProperties);
        AdminCreateAdvt advt = new AdminCreateAdvt();
        advt.setName("Advt");
        advt.setUserId(user.getId());
        advt.setProduct(product);

        ResponseEntity<AdvtInfo> response = createAdvt(advt, List.of(), AdvtInfo.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        AdvtInfo actualAdvt = response.getBody();
        AdvtInfo expectedAdvt = fromAdminCreateAdvt(1L, advt, List.of(), LocalDate.now());
        assertThat(actualAdvt).isEqualTo(expectedAdvt);
    }


    @DisplayName("Обновление объявления пользователя")
    @Test
    public void updateTest() {
        AdminUserProfileInfo user = saveDefaultUserAndGet();
        AdvtInfo expectedAdvt = saveDefaultAdvtAndGet(user.getCredentials().getLogin());
        List<Long> removingAttachments = List.of(1L, 2L);
        UpdateProduct updateProduct = createUpdateProduct(expectedAdvt.getProduct(), removingAttachments);
        AdminUpdateAdvt updateAdvt = createAdminUpdateAdvt(user.getId(), expectedAdvt.getId(), "newName", updateProduct);
        expectedAdvt.setName(updateAdvt.getName());
        expectedAdvt.getProduct().getAttachments().removeAll(removingAttachments);

        ResponseEntity<AdvtInfo> response = updateAdvt(updateAdvt, List.of(), AdvtInfo.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        AdvtInfo actualAdvt = response.getBody();
        assertThat(actualAdvt).isEqualTo(expectedAdvt);
        for (Long attId : removingAttachments) {
            ResponseEntity<String> attachment = getAttachment(attId, String.class);
            assertThat(attachment.getStatusCode()).isEqualTo(NOT_FOUND);
        }
    }

    @DisplayName("Удаление по id")
    @Test
    public void deleteByIdTest() {
        AdminUserProfileInfo user = saveDefaultUserAndGet();
        AdvtInfo advt = saveDefaultAdvtAndGet(user.getCredentials().getLogin());
        ResponseEntity<Void> response = deleteById(advt.getId(), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        ResponseEntity<String> responseAfterDelete = getById(advt.getId(), String.class);
        assertThat(responseAfterDelete.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    private <T> ResponseEntity<T> getById(Long id, Class<T> type) {
        return restTemplate.getForEntity("/api/v1/advertisement/{id}", type, id);
    }

    private <T> ResponseEntity<T> createAdvt(AdminCreateAdvt advt, List<byte[]> files, Class<T> type) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        files.forEach(file -> {
            ByteArrayResource resource = createByteArrayResource(file);
            HttpHeaders fileHeaders = new HttpHeaders();
            fileHeaders.add(CONTENT_TYPE, IMAGE_JPEG_VALUE);
            HttpEntity<ByteArrayResource> entity = new HttpEntity<>(resource, fileHeaders);
            body.add("files", entity);
        });
        body.add("advertisement", advt);
        RequestEntity<MultiValueMap<String, Object>> request = post("/api/v1/admin/advertisement")
                .header(AUTHORIZATION, adminAuthHeader())
                .contentType(MULTIPART_FORM_DATA)
                .body(body);
        return restTemplate.exchange(request, type);
    }

    private <T> ResponseEntity<T> updateAdvt(AdminUpdateAdvt advtDto, List<byte[]> files, Class<T> type) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        files.forEach(file -> {
            ByteArrayResource resource = createByteArrayResource(file);
            HttpHeaders fileHeaders = new HttpHeaders();
            fileHeaders.add(CONTENT_TYPE, IMAGE_JPEG_VALUE);
            HttpEntity<ByteArrayResource> entity = new HttpEntity<>(resource, fileHeaders);
            body.add("files", entity);
        });
        body.add("advertisement", advtDto);
        RequestEntity<MultiValueMap<String, Object>> request = put("/api/v1/admin/advertisement")
                .header(AUTHORIZATION, adminAuthHeader())
                .contentType(MULTIPART_FORM_DATA)
                .body(body);
        return restTemplate.exchange(request, type);
    }

    private <T> ResponseEntity<T> deleteById(Long id, Class<T> type) {
        RequestEntity<Void> request = delete("/api/v1/admin/advertisement/{id}", id)
                .header(AUTHORIZATION, adminAuthHeader())
                .build();
        return restTemplate.exchange(request, type);
    }

    private <T> ResponseEntity<T> getAttachment(Long id, Class<T> type) {
        RequestEntity<Void> request = get("/api/v1/attachment/{id}", id).build();
        return restTemplate.exchange(request, type);
    }

}
