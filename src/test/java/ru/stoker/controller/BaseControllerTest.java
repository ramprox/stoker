package ru.stoker.controller;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.stoker.anotations.ControllerIT;
import ru.stoker.database.entity.User;
import ru.stoker.database.entity.embeddable.Credentials;
import ru.stoker.database.entity.embeddable.PersonalData;
import ru.stoker.database.entity.enums.Role;
import ru.stoker.database.repository.UserRepository;
import ru.stoker.dto.advt.AdvtInfo;
import ru.stoker.dto.advt.CreateAdvt;
import ru.stoker.dto.category.CategoryDto;
import ru.stoker.dto.product.CreateProduct;
import ru.stoker.dto.product.productproperties.ProductPropertiesDto;
import ru.stoker.dto.product.productproperties.notebook.NotebookPropertiesDto;
import ru.stoker.dto.product.productproperties.notebook.ProcessorDto;
import ru.stoker.dto.productestimaton.ProductEstimationDto;
import ru.stoker.dto.profile.AdminUserProfileInfo;
import ru.stoker.dto.userestimation.UserEstimationDto;
import ru.stoker.util.builder.CredentialsBuilder;
import ru.stoker.util.builder.DatabaseFacade;
import ru.stoker.util.builder.PersonalDataBuilder;
import ru.stoker.util.factory.AdminProfileFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.http.RequestEntity.post;
import static ru.stoker.util.builder.CredentialsBuilder.credentials;
import static ru.stoker.util.builder.PersonalDataBuilder.personalData;
import static ru.stoker.util.builder.UserBuilder.user;
import static ru.stoker.util.factory.ByteArrayResourceFactory.createByteArrayResource;
import static ru.stoker.util.factory.CategoryDtoFactory.categoryDto;
import static ru.stoker.util.factory.AdvtDtosFactory.getCreateAdvt;
import static ru.stoker.util.factory.AdvtDtosFactory.getCreateProduct;

@ControllerIT
public class BaseControllerTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    private DatabaseFacade databaseFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Random random = new Random();

    protected final AdminProfileFactory adminProfileFactory = new AdminProfileFactory();

    protected User admin;

    @BeforeEach
    public void beforeEach() {
        databaseFacade.clean();
        admin = saveAdmin();
    }

    @AfterEach
    public void afterEach(@Autowired Path storagePath) throws IOException {
        try (Stream<Path> walk = Files.list(storagePath)) {
            Iterator<Path> iterator = walk.iterator();
            while (iterator.hasNext()) {
                Path next = iterator.next();
                FileSystemUtils.deleteRecursively(next);
            }
        }
    }

    protected AdminUserProfileInfo saveDefaultUserAndGet() {
        AdminUserProfileInfo createProfileReq = adminProfileFactory.defaultProfileInfo();
        return saveUser(createProfileReq, AdminUserProfileInfo.class).getBody();
    }

    protected <T> ResponseEntity<T> saveUser(AdminUserProfileInfo user, Class<T> type) {
        RequestEntity<AdminUserProfileInfo> reqEntity = post("/api/v1/admin/profile")
                .header(AUTHORIZATION, adminAuthHeader())
                .body(user);
        return restTemplate.exchange(reqEntity, type);
    }

    protected AdminUserProfileInfo saveUserAndGet(AdminUserProfileInfo user) {
        return saveUser(user, AdminUserProfileInfo.class).getBody();
    }

    public String token(String login) {
        return Jwts.builder()
                .addClaims(Map.of("login", login))
                .signWith(SignatureAlgorithm.HS256, "stoker")
                .compact();
    }

    protected String adminAuthHeader() {
        return userAuthHeader(admin.getCredentials().getLogin());
    }

    protected String userAuthHeader(String login) {
        return "Bearer " + token(login);
    }

    private User saveAdmin() {
        CredentialsBuilder credentials = credentials()
                .withLogin("admin")
                .withPassword(passwordEncoder.encode("admin"));
        PersonalDataBuilder personalData = personalData()
                .withPhone("87777777777")
                .withEmail("admin@mail.ru");
        User admin = user()
                .withCredentials(credentials)
                .withPersonalData(personalData)
                .withRole(Role.ADMIN)
                .build();
        databaseFacade.executeInTransaction(() -> userRepository.save(admin));
        return admin;
    }

    protected CategoryDto saveCategoryAndGet(CategoryDto categoryDto) {
        return saveCategory(categoryDto, CategoryDto.class).getBody();
    }

    protected <T> ResponseEntity<T> saveCategory(CategoryDto category, Class<T> bodyType) {
        RequestEntity<CategoryDto> request = post("/api/v1/category")
                .header(AUTHORIZATION, adminAuthHeader())
                .body(category);
        return restTemplate.exchange(request, bodyType);
    }

    protected <T> ResponseEntity<T> saveCategory(CategoryDto category, ParameterizedTypeReference<T> type) {
        RequestEntity<CategoryDto> request = post("/api/v1/category")
                .header(AUTHORIZATION, adminAuthHeader())
                .body(category);
        return restTemplate.exchange(request, type);
    }

    protected AdvtInfo saveDefaultAdvtAndGet(String login) {
        CategoryDto category = saveCategoryAndGet(categoryDto("Category"));
        ProductPropertiesDto properties = getDefaultNotebookProperties();
        CreateProduct product = getCreateProduct(category.getId(), new BigDecimal("100"), "Description", properties);
        CreateAdvt advt = getCreateAdvt("Name", product);
        List<byte[]> files = List.of(
                randomBytes(10),
                randomBytes(20)
        );
        return saveAdvt(advt, files, login, AdvtInfo.class).getBody();
    }

    protected List<AdvtInfo> saveDefaultAdvtAndGet(String login, int count) {
        CategoryDto category = saveCategoryAndGet(categoryDto("Category"));
        List<AdvtInfo> result = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            ProductPropertiesDto properties = getDefaultNotebookProperties();
            CreateProduct product = getCreateProduct(category.getId(), new BigDecimal("100"), "Description", properties);
            CreateAdvt advt = getCreateAdvt("Name", product);
            List<byte[]> files = List.of(
                    randomBytes(10),
                    randomBytes(20)
            );
            AdvtInfo advtInfo = saveAdvt(advt, files, login, AdvtInfo.class).getBody();
            result.add(advtInfo);
        }
        return result;
    }

    protected <T> ResponseEntity<T> saveAdvt(CreateAdvt advertisement, List<byte[]> files,
                                             String login, Class<T> type) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        files.forEach(file -> {
            ByteArrayResource resource = createByteArrayResource(file);
            HttpHeaders fileHeaders = new HttpHeaders();
            fileHeaders.add(CONTENT_TYPE, IMAGE_JPEG_VALUE);
            HttpEntity<ByteArrayResource> entity = new HttpEntity<>(resource, fileHeaders);
            body.add("files", entity);
        });
        body.add("advertisement", advertisement);
        RequestEntity<MultiValueMap<String, Object>> request = post("/api/v1/advertisement")
                .header(AUTHORIZATION, userAuthHeader(login))
                .contentType(MULTIPART_FORM_DATA)
                .body(body);
        return restTemplate.exchange(request, type);
    }

    protected <T> ResponseEntity<T> saveFavorite(Long advtId, String login, Class<T> type) {
        RequestEntity<Long> request = post("/api/v1/favorite")
                .header(AUTHORIZATION, userAuthHeader(login))
                .body(advtId);
        return restTemplate.exchange(request, type);
    }

    protected <T> ResponseEntity<T> estimateProduct(ProductEstimationDto productEstimationDto,
                                                    String login, Class<T> type) {
        RequestEntity<ProductEstimationDto> request = post("/api/v1/estimation/product")
                .header(AUTHORIZATION, userAuthHeader(login))
                .body(productEstimationDto);
        return restTemplate.exchange(request, type);
    }

    protected <T> ResponseEntity<T> saveEstimation(UserEstimationDto userEstimationDto,
                                                   String login, Class<T> type) {
        RequestEntity<UserEstimationDto> request = post("/api/v1/estimation/user")
                .header(AUTHORIZATION, userAuthHeader(login))
                .body(userEstimationDto);
        return restTemplate.exchange(request, type);
    }

    protected NotebookPropertiesDto getDefaultNotebookProperties() {
        NotebookPropertiesDto properties = new NotebookPropertiesDto();
        properties.setManufacturer("Man");
        ProcessorDto processor = new ProcessorDto();
        processor.setModel("AMD");
        processor.setKernelCount(2);
        processor.setFrequency(2.5);
        properties.setProcessor(processor);
        properties.setOs("Linux");
        properties.setRam(4);
        properties.setScreenDiagonal(15.6);
        return properties;
    }

    protected byte[] randomBytes(int length) {
        byte[] result = new byte[length];
        random.nextBytes(result);
        return result;
    }

    protected List<AdminUserProfileInfo> saveDefaultUsers(int count) {
        List<AdminUserProfileInfo> profileReqs = adminProfileFactory.defaultProfiles(count);
        List<AdminUserProfileInfo> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            users.add(saveUserAndGet(profileReqs.get(i)));
        }
        return users;
    }

}
