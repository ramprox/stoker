package ru.stoker.database.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.stoker.anotations.RepositoryIT;
import ru.stoker.database.entity.*;
import ru.stoker.database.entity.embeddable.Credentials;
import ru.stoker.database.entity.embeddable.Estimation;
import ru.stoker.database.entity.embeddable.PersonalData;
import ru.stoker.database.entity.embeddable.ProductEstimationId;
import ru.stoker.util.builder.DatabaseFacade;
import ru.stoker.util.factory.CategoryFactory;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.stoker.util.builder.CredentialsBuilder.credentials;
import static ru.stoker.util.builder.PersonalDataBuilder.personalData;

@DisplayName("Интеграционные тесты для ProductRepository")
@RepositoryIT
public class ProductEstimationRepositoryTest {

    @Autowired
    private ProductEstimationRepository productEstimationRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private DatabaseFacade databaseFacade;

    @BeforeEach
    public void beforeEach() {
        databaseFacade.executeInTransaction(() -> {
            databaseFacade.clean();
            insertInitData();
        });
    }

    @DisplayName("Сохранение оценки товара")
    @Test
    public void saveTest() {
        ProductEstimationId id = new ProductEstimationId(1L, 2L);
        ProductEstimation productEstimation =
                new ProductEstimation(id, new Estimation(5, LocalDateTime.now()));
        productEstimationRepository.save(productEstimation);

        Optional<ProductEstimation> actualProductEstOpt =
                productEstimationRepository.findById(new ProductEstimationId(1L, 2L));
        assertThat(actualProductEstOpt.isPresent()).isTrue();
    }

    private void insertInitData() {
        Category category = categoryRepository.save(CategoryFactory.category("Category"));

        Credentials credentials = credentials().withLogin("login1").build();
        PersonalData personalData = personalData().withEmail("email1@email.ru")
                .withPhone("89999999991").build();
        User user = new User(credentials, personalData);
        user.setCreatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        Advertisement advertisement = new Advertisement("Advertisement1", LocalDate.now(), user);
        Product product = new Product(category, new BigDecimal(100));
        advertisement.setProduct(product);
        advertisementRepository.save(advertisement);

        credentials = credentials().withLogin("login2").build();
        personalData = personalData().withEmail("email2@email.ru")
                .withPhone("89999999992").build();
        user = new User(credentials, personalData);
        user.setCreatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        advertisement = new Advertisement("Advertisement2", LocalDate.now(), user);
        product = new Product(category, new BigDecimal(100));
        advertisement.setProduct(product);
        advertisementRepository.save(advertisement);
    }



}
