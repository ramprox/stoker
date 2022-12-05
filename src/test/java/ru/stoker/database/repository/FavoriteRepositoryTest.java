package ru.stoker.database.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.stoker.anotations.RepositoryIT;
import ru.stoker.database.entity.*;
import ru.stoker.database.entity.embeddable.Credentials;
import ru.stoker.database.entity.embeddable.FavoriteId;
import ru.stoker.database.entity.embeddable.PersonalData;
import ru.stoker.util.builder.DatabaseFacade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.stoker.util.builder.CredentialsBuilder.credentials;
import static ru.stoker.util.builder.PersonalDataBuilder.personalData;

@DisplayName("Интеграционные тесты для FavoriteRepository")
@RepositoryIT
public class FavoriteRepositoryTest {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

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

    @DisplayName("Сохранение объявления в избранном")
    @Test
    public void saveTest() {
        Favorite favorite = new Favorite(new FavoriteId(1L, 2L));

        favoriteRepository.save(favorite);

        Optional<Favorite> actualFavorite = favoriteRepository.findById(favorite.getId());
        assertThat(actualFavorite.isPresent()).isEqualTo(true);
    }

    @DisplayName("Извлечение помещенных в избранное объявлений по id пользователя")
    @Test
    public void findFavoritesByUserIdTest() {
        List<Favorite> expectedFavorites = favoriteRepository.saveAll(List.of(
                new Favorite(new FavoriteId(1L, 1L)),
                new Favorite(new FavoriteId(1L, 2L))));

        databaseFacade.executeInTransaction(() -> {
            List<Favorite> actualFavorites = favoriteRepository.findAllByIdUserId(1L);
            assertThat(actualFavorites).isEqualTo(expectedFavorites);
        });
    }

    private void insertInitData() {
        Category category = new Category();
        category.setName("Category");
        Category savedCategory = categoryRepository.save(category);

        Credentials credentials = credentials().withLogin("login1").build();
        PersonalData personalData = personalData().withEmail("email1@email.ru")
                .withPhone("89999999991").build();
        User user = new User(credentials, personalData);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
        user = userRepository.save(user);

        Advertisement advertisement = new Advertisement("Advertisement1", LocalDate.now(), user);
        Product product = new Product(savedCategory, new BigDecimal(100));
        advertisement.setProduct(product);
        advertisementRepository.save(advertisement);

        credentials = credentials().withLogin("login2").build();
        personalData = personalData().withEmail("email2@email.ru")
                .withPhone("89999999992").build();
        user = new User(credentials, personalData);
        user.setCreatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        advertisement = new Advertisement("Advertisement2", LocalDate.now(), user);
        product = new Product(savedCategory, new BigDecimal(100));
        advertisement.setProduct(product);
        advertisementRepository.save(advertisement);
    }

}
