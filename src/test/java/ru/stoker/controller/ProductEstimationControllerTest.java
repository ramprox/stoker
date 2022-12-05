package ru.stoker.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import ru.stoker.dto.advt.AdvtInfo;
import ru.stoker.dto.estimation.EstimationDto;
import ru.stoker.dto.estimation.EstimationInfo;
import ru.stoker.dto.productestimaton.ProductEstimationDto;
import ru.stoker.dto.productestimaton.ProductEstimationInfo;
import ru.stoker.dto.profile.AdminUserProfileInfo;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.RequestEntity.*;
import static ru.stoker.util.factory.EstimationDtosFactory.estimationDto;
import static ru.stoker.util.factory.EstimationDtosFactory.estimationInfo;
import static ru.stoker.util.factory.ProductEstimationDtosFactory.productEstimationDto;
import static ru.stoker.util.factory.ProductEstimationDtosFactory.productEstimationInfo;

@DisplayName("Интеграционные тесты ProductEstimationController")
public class ProductEstimationControllerTest extends BaseControllerTest {

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

    @DisplayName("Оценивание товара пользователем")
    @Test
    public void estimateTest() {
        AdvtInfo advtDto = saveDefaultAdvtAndGet(user1.getCredentials().getLogin());
        EstimationDto estimationDto = estimationDto(5, "Comment");
        ProductEstimationDto productEstimationDto = productEstimationDto(advtDto.getId(), estimationDto);

        ResponseEntity<ProductEstimationInfo> response = estimateProduct(productEstimationDto,
                user2.getCredentials().getLogin(), ProductEstimationInfo.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);

        ProductEstimationInfo actualProductEstimation = response.getBody();
        EstimationInfo expectedEstimationDto = estimationInfo(estimationDto, null);
        ProductEstimationInfo expectedProductEstimation =
                productEstimationInfo(advtDto.getId(), user2.getId(), expectedEstimationDto);
        assertThat(actualProductEstimation).isEqualTo(expectedProductEstimation);
    }

    @DisplayName("Извлечение оценки товара по id товара и id пользователя")
    @Test
    public void getByIdTest() {
        AdvtInfo advtDto = saveDefaultAdvtAndGet(user1.getCredentials().getLogin());
        EstimationDto estimationDto = estimationDto(5, "Comment");
        ProductEstimationDto productEstimationDto = productEstimationDto(advtDto.getId(), estimationDto);

        estimateProduct(productEstimationDto, user2.getCredentials().getLogin(), ProductEstimationInfo.class);

        ResponseEntity<ProductEstimationInfo> response = getByUserIdAndProductId(user2.getId(),
                advtDto.getId(), ProductEstimationInfo.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);

        ProductEstimationInfo actualProductEstimation = response.getBody();
        EstimationInfo expectedEstimationDto = estimationInfo(estimationDto, null);
        ProductEstimationInfo expectedProductEstimation =
                productEstimationInfo(advtDto.getId(), user2.getId(), expectedEstimationDto);
        assertThat(actualProductEstimation).isEqualTo(expectedProductEstimation);
    }

    @DisplayName("Извлечение оценок товара по его id")
    @Test
    public void getAllByProductIdTest() {
        AdvtInfo advtDto = saveDefaultAdvtAndGet(user1.getCredentials().getLogin());
        EstimationDto estimationDto = estimationDto(5, "Comment");
        ProductEstimationDto productEstimationDto = productEstimationDto(advtDto.getId(), estimationDto);
        estimateProduct(productEstimationDto, user2.getCredentials().getLogin(), ProductEstimationInfo.class);

        ResponseEntity<List<ProductEstimationInfo>> response = getByProductId(advtDto.getId(),
                new ParameterizedTypeReference<>() { });

        assertThat(response.getStatusCode()).isEqualTo(OK);
        List<ProductEstimationInfo> actualProductEstimations = response.getBody();
        EstimationInfo expectedEstimationDto = estimationInfo(estimationDto, null);
        ProductEstimationInfo expectedProductEstimation =
                productEstimationInfo(advtDto.getId(), user2.getId(), expectedEstimationDto);
        List<ProductEstimationInfo> expectedEstimations = List.of(expectedProductEstimation);
        assertThat(actualProductEstimations).isEqualTo(expectedEstimations);
    }

    @DisplayName("Обновление оценки товара")
    @Test
    public void updateProductEstimationTest() {
        AdvtInfo advtDto = saveDefaultAdvtAndGet(user1.getCredentials().getLogin());
        EstimationDto estimationDto = estimationDto(5, "Comment");
        ProductEstimationDto productEstimationDto = productEstimationDto(advtDto.getId(), estimationDto);

        ProductEstimationInfo productEstimationInfo =
                estimateProduct(productEstimationDto, user2.getCredentials().getLogin(),
                ProductEstimationInfo.class).getBody();

        productEstimationDto.getEstimation().setValue(10);
        productEstimationDto.getEstimation().setComment("New comment");
        productEstimationInfo.getEstimation().setValue(10);
        productEstimationInfo.getEstimation().setComment("New comment");

        ResponseEntity<ProductEstimationInfo> response = updateProductEstimation(productEstimationDto,
                user2.getCredentials().getLogin(), ProductEstimationInfo.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        ProductEstimationInfo actualEstimation = response.getBody();
        assertThat(actualEstimation).isEqualTo(productEstimationInfo);
    }

    @DisplayName("Удаление оценки по id товара")
    @Test
    public void deleteByProductIdTest() {
        AdvtInfo advtDto = saveDefaultAdvtAndGet(user1.getCredentials().getLogin());
        EstimationDto estimationDto = estimationDto(5, "Comment");
        ProductEstimationDto productEstimationDto = productEstimationDto(advtDto.getId(), estimationDto);
        estimateProduct(productEstimationDto, user2.getCredentials().getLogin(), ProductEstimationInfo.class);

        ResponseEntity<Void> response = deleteByProductId(advtDto.getId(), user2.getCredentials().getLogin(), Void.class);

        assertThat(response.getStatusCode()).isEqualTo(OK);
        ResponseEntity<String> responseAfterDelete = getByUserIdAndProductId(user2.getId(),
                advtDto.getId(), String.class);
        assertThat(responseAfterDelete.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    private <T> ResponseEntity<T> getByUserIdAndProductId(Long userId, Long productId, Class<T> type) {
        return restTemplate.getForEntity("/api/v1/estimation/product/{productId}/{userId}", type, productId, userId);
    }

    private <T> ResponseEntity<T> getByProductId(Long productId, ParameterizedTypeReference<T> type) {
        RequestEntity<Void> request = get("/api/v1/estimation/product/all?productId={productId}", productId)
                .build();
        return restTemplate.exchange(request, type);
    }

    private <T> ResponseEntity<T> updateProductEstimation(ProductEstimationDto productEstimationDto,
                                                  String login, Class<T> type) {
        RequestEntity<ProductEstimationDto> request = put("/api/v1/estimation/product")
                .header(AUTHORIZATION, userAuthHeader(login))
                .body(productEstimationDto);
        return restTemplate.exchange(request, type);
    }

    private <T> ResponseEntity<T> deleteByProductId(Long productId, String login, Class<T> type) {
        RequestEntity<Void> request = delete("/api/v1/estimation/product/{productId}", productId)
                .header(AUTHORIZATION, userAuthHeader(login))
                .build();
        return restTemplate.exchange(request, type);
    }

}
