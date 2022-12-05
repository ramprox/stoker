package ru.stoker.database.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.stoker.anotations.RepositoryIT;
import ru.stoker.database.entity.Advertisement;
import ru.stoker.database.entity.Category;
import ru.stoker.database.entity.Product;
import ru.stoker.database.entity.User;
import ru.stoker.database.entity.embeddable.Credentials;
import ru.stoker.database.entity.embeddable.PersonalData;
import ru.stoker.database.entity.productproperties.ProductProperties;
import ru.stoker.database.entity.productproperties.apartment.ApartmentArea;
import ru.stoker.database.entity.productproperties.apartment.ApartmentProperties;
import ru.stoker.database.entity.productproperties.apartment.BathroomType;
import ru.stoker.util.builder.DatabaseFacade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.stoker.util.builder.CredentialsBuilder.credentials;
import static ru.stoker.util.builder.PersonalDataBuilder.personalData;

@DisplayName("Интеграционные тесты для AdvertisementRepository")
@RepositoryIT
public class AdvertisementRepositoryTest {

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
        Advertisement advertisement = new Advertisement("Name", LocalDate.now(), user);
        Product product = new Product(category, new BigDecimal(100));
        ApartmentArea area = new ApartmentArea();
        area.setKitchenArea(5);
        area.setLivingArea(5);
        area.setTotalArea(10);
        ApartmentProperties expectedProperties = new ApartmentProperties();
        expectedProperties.setApartmentArea(area);
        expectedProperties.setCeilingHeight(2.6);
        expectedProperties.setBathroomType(BathroomType.COMBINED);
        expectedProperties.setRoomCount(1);
        expectedProperties.setFloor(4);
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
        User user = new User(credentials, personalData);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        Category category = new Category();
        category.setName("Category");
        categoryRepository.save(category);
    }

}
