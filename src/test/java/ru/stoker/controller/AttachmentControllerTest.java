package ru.stoker.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import ru.stoker.dto.advt.AdvtInfo;
import ru.stoker.dto.profile.AdminUserProfileInfo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.type;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.http.RequestEntity.delete;
import static org.springframework.http.RequestEntity.get;
import static ru.stoker.util.factory.ByteArrayResourceFactory.createByteArrayResource;

@DisplayName("Интеграционные тесты AttachmentController")
public class AttachmentControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("Сохранение картинки")
    public void saveTest() {
        AdminUserProfileInfo user = saveDefaultUserAndGet();
        AdvtInfo savedAdvt = saveDefaultAdvtAndGet(user.getCredentials().getLogin());

        ResponseEntity<Long> response = saveAttachment(user.getCredentials().getLogin(),
                savedAdvt.getId(), new byte[] { 10, 20 }, Long.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Получение картинки")
    public void getAttachment() {
        AdminUserProfileInfo user = saveDefaultUserAndGet();
        AdvtInfo savedAdvt = saveDefaultAdvtAndGet(user.getCredentials().getLogin());

        ResponseEntity<Long> response = saveAttachment(user.getCredentials().getLogin(),
                savedAdvt.getId(), new byte[] { 10, 20 }, Long.class);
        ResponseEntity<byte[]> attachment = getAttachment(response.getBody(), byte[].class);
        assertThat(attachment.getStatusCode()).isEqualTo(OK);
        assertThat(attachment.getBody()).isEqualTo(new byte[] { 10, 20 });
    }

    @Test
    @DisplayName("Удаление картинки")
    public void deleteAttachment() {
        AdminUserProfileInfo user = saveDefaultUserAndGet();
        AdvtInfo savedAdvt = saveDefaultAdvtAndGet(user.getCredentials().getLogin());
        Long attId = saveAttachment(user.getCredentials().getLogin(),
                savedAdvt.getId(), new byte[]{10, 20}, Long.class).getBody();
        ResponseEntity<Void> response = deleteAttachment(user.getCredentials().getLogin(), attId, Void.class);
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    private <T> ResponseEntity<T> saveAttachment(String login, Long productId, byte[] content, Class<T> type) {
        LinkedMultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("file", createByteArrayResource(content));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MULTIPART_FORM_DATA);
        headers.add(AUTHORIZATION, userAuthHeader(login));
        headers.add("Image-type", "image/jpeg");
        HttpEntity<LinkedMultiValueMap<String, Object>> entity = new HttpEntity<>(params, headers);

        return restTemplate.exchange("/api/v1/attachment/{productId}", POST, entity, type, productId);
    }

    private <T> ResponseEntity<T> getAttachment(Long id, Class<T> type) {
        RequestEntity<Void> request = get("/api/v1/attachment/{id}", id).build();
        return restTemplate.exchange(request, type);
    }

    private <T> ResponseEntity<T> deleteAttachment(String login, Long id, Class<T> type) {
        RequestEntity<Void> request = delete("/api/v1/attachment/{id}", id)
                .header(AUTHORIZATION, userAuthHeader(login))
                .build();
        return restTemplate.exchange(request, type);
    }

}
