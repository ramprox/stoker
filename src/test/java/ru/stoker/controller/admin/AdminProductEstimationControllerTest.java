package ru.stoker.controller.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import ru.stoker.controller.BaseControllerTest;
import ru.stoker.dto.advt.AdvtInfo;
import ru.stoker.dto.estimation.EstimationDto;
import ru.stoker.dto.productestimaton.ProductEstimationDto;
import ru.stoker.dto.productestimaton.ProductEstimationIdDto;
import ru.stoker.dto.productestimaton.ProductEstimationInfo;
import ru.stoker.dto.productestimaton.UpdateProductEstimation;
import ru.stoker.dto.profile.AdminUserProfileInfo;
import ru.stoker.util.factory.ProductEstimationDtosFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static ru.stoker.util.factory.EstimationDtosFactory.estimationDto;
import static ru.stoker.util.factory.ProductEstimationDtosFactory.productEstimationDto;

@DisplayName("Интеграционные тесты AdminProductEstimationController")
public class AdminProductEstimationControllerTest extends BaseControllerTest {

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

    @DisplayName("Обновление оценки продукта пользователя")
    @Test
    public void updateTest() {
        AdvtInfo advtDto = saveDefaultAdvtAndGet(user1.getCredentials().getLogin());
        EstimationDto estimationDto = estimationDto(5, "Comment");
        ProductEstimationDto productEstimationDto = productEstimationDto(advtDto.getId(), estimationDto);

        ProductEstimationInfo productEstimationInfo = estimateProduct(productEstimationDto,
                user2.getCredentials().getLogin(), ProductEstimationInfo.class).getBody();

        UpdateProductEstimation updateProductEst = ProductEstimationDtosFactory.updateProductEstimation(user2.getId(), productEstimationDto);
        updateProductEst.getEstimation().setValue(10);
        updateProductEst.getEstimation().setComment("New comment");
        productEstimationInfo.getEstimation().setValue(10);
        productEstimationInfo.getEstimation().setComment("New comment");

        ResponseEntity<ProductEstimationInfo> response = updateProductEstimation(updateProductEst,
                ProductEstimationInfo.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        ProductEstimationInfo actualEstimation = response.getBody();
        assertThat(actualEstimation).isEqualTo(productEstimationInfo);
    }

    @DisplayName("Удаление оценки продукта пользователя по id оценки")
    @Test
    public void deleteByIdTest() {
        AdvtInfo advtDto = saveDefaultAdvtAndGet(user1.getCredentials().getLogin());
        EstimationDto estimationDto = estimationDto(5, "Comment");
        ProductEstimationDto productEstimationDto = productEstimationDto(advtDto.getId(), estimationDto);
        estimateProduct(productEstimationDto, user2.getCredentials().getLogin(), ProductEstimationInfo.class);

        ProductEstimationIdDto id = new ProductEstimationIdDto();
        id.setUserId(user2.getId());
        id.setProductId(advtDto.getId());
        ResponseEntity<Void> response = deleteById(id, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        ResponseEntity<String> responseAfterDelete = getByUserIdAndProductId(user2.getId(), advtDto.getId(), String.class);
        assertThat(responseAfterDelete.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    private <T> ResponseEntity<T> updateProductEstimation(ProductEstimationDto productEstimationDto, Class<T> type) {
        RequestEntity<ProductEstimationDto> request = RequestEntity.put("/api/v1/admin/estimation/product")
                .header(AUTHORIZATION, adminAuthHeader())
                .body(productEstimationDto);
        return restTemplate.exchange(request, type);
    }

    private <T> ResponseEntity<T> deleteById(ProductEstimationIdDto id, Class<T> type) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION, adminAuthHeader());
        HttpEntity<ProductEstimationIdDto> request = new HttpEntity<>(id, headers);
        return restTemplate.exchange("/api/v1/admin/estimation/product", DELETE, request, type);
    }

    private <T> ResponseEntity<T> getByUserIdAndProductId(Long userId, Long productId, Class<T> type) {
        return restTemplate.getForEntity("/api/v1/estimation/product/{productId}/{userId}", type, productId, userId);
    }

}
