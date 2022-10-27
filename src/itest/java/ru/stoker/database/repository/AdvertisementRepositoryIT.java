package ru.stoker.database.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.stoker.annotations.RepositoryIT;
import ru.stoker.database.entity.Advertisement;
import ru.stoker.database.entity.Category;
import ru.stoker.database.entity.Product;
import ru.stoker.database.entity.User;
import ru.stoker.database.entity.embeddable.Credentials;
import ru.stoker.database.entity.embeddable.FullName;
import ru.stoker.database.entity.embeddable.PersonalData;
import ru.stoker.database.entity.productproperties.ApartmentArea;
import ru.stoker.database.entity.productproperties.ApartmentProperties;
import ru.stoker.database.entity.productproperties.BathroomType;
import ru.stoker.database.entity.productproperties.ProductProperties;
import ru.stoker.util.DatabaseFacade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.stoker.util.CredentialsBuilder.credentials;
import static ru.stoker.util.PersonalDataBuilder.personalData;

@DisplayName("Интеграционные тесты для AdvertisementRepository")
@RepositoryIT
public class AdvertisementRepositoryIT {

    @Autowired
    private AdvertisementRepository advertisementRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DatabaseFacade databaseFacade;

    @BeforeEach
    public void beforeEach() {
        databaseFacade.clean();
        databaseFacade.executeInTransaction(this::insertInitData);
    }

    @DisplayName("Сохранение объявления")
    @Test
    public void saveAdvertisementTest() {
        User user = userRepository.getReferenceById(1L);
        Category category = categoryRepository.getReferenceById(1L);
        Advertisement advertisement = new Advertisement("Name", new Date(), user);
        Product product = new Product(category, new BigDecimal(100));
        ApartmentArea area = new ApartmentArea(10, 5, 5);
        ProductProperties expectedProperties = new ApartmentProperties(4, area, 1, 2.6, BathroomType.COMBINED);
        product.setProperties(expectedProperties);
        advertisement.setProduct(product);

        advertisementRepository.save(advertisement);

        Optional<Advertisement> actualAdvertisementOpt = advertisementRepository.findById(1L);
        assertThat(actualAdvertisementOpt.isPresent()).isEqualTo(true);
        Product actualProduct = actualAdvertisementOpt.get().getProduct();
        assertThat(actualProduct).isNotNull();
        ProductProperties actualProductProperties = actualProduct.getProperties();
        assertThat(actualProductProperties).isEqualTo(expectedProperties);
    }

    private void insertInitData() {
        Credentials credentials = credentials().build();
        PersonalData personalData = personalData().build();
        userRepository.save(new User(credentials, personalData));
        categoryRepository.save(new Category("Category"));
    }

}
