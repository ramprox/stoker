package ru.stoker.controller.admin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
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
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.RequestEntity.delete;
import static org.springframework.http.RequestEntity.get;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.http.RequestEntity.put;
import static ru.stoker.util.factory.AdvtDtosFactory.createAdminUpdateAdvt;
import static ru.stoker.util.factory.AdvtDtosFactory.createUpdateProduct;
import static ru.stoker.util.factory.AdvtDtosFactory.fromAdminCreateAdvt;
import static ru.stoker.util.factory.AdvtDtosFactory.getCreateProduct;
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

        ResponseEntity<AdvtInfo> response = createAdvt(advt, AdvtInfo.class);
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
        List<Long> removingAttachments = List.of();
        UpdateProduct updateProduct = createUpdateProduct(expectedAdvt.getProduct(), removingAttachments);
        AdminUpdateAdvt updateAdvt = createAdminUpdateAdvt(user.getId(), expectedAdvt.getId(), "newName", updateProduct);
        expectedAdvt.setName(updateAdvt.getName());
        expectedAdvt.getProduct().getAttachments().removeAll(removingAttachments);

        ResponseEntity<AdvtInfo> response = updateAdvt(updateAdvt, AdvtInfo.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        AdvtInfo actualAdvt = response.getBody();
        assertThat(actualAdvt).isEqualTo(expectedAdvt);
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

    private <T> ResponseEntity<T> createAdvt(AdminCreateAdvt advt, Class<T> type) {
        RequestEntity<AdminCreateAdvt> request = post("/api/v1/admin/advertisement")
                .header(AUTHORIZATION, adminAuthHeader())
                .body(advt);
        return restTemplate.exchange(request, type);
    }

    private <T> ResponseEntity<T> updateAdvt(AdminUpdateAdvt advtDto, Class<T> type) {
        RequestEntity<AdminUpdateAdvt> request = put("/api/v1/admin/advertisement")
                .header(AUTHORIZATION, adminAuthHeader())
                .body(advtDto);
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
