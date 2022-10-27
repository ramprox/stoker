package ru.stoker.database.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.stoker.annotations.RepositoryIT;
import ru.stoker.database.entity.*;
import ru.stoker.database.entity.embeddable.Credentials;
import ru.stoker.database.entity.embeddable.PersonalData;
import ru.stoker.util.DatabaseFacade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.stoker.util.CredentialsBuilder.credentials;
import static ru.stoker.util.PersonalDataBuilder.personalData;

@DisplayName("Интеграционные тесты для FavoriteRepository")
@RepositoryIT
public class FavoriteRepositoryIT {

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
        Favorite favorite = new Favorite(1L, 2L);

        favoriteRepository.save(favorite);

        Optional<Favorite> actualFavorite = favoriteRepository.findById(favorite.getFavoriteId());
        assertThat(actualFavorite.isPresent()).isEqualTo(true);
    }

    @DisplayName("Извлечение помещенных в избранное объявлений по id пользователя ()")
    @Test
    public void findFavoritesByUserIdTest() {
        List<Favorite> expectedFavorites = favoriteRepository.saveAll(List.of(
                new Favorite(1L, 1L),
                new Favorite(1L, 2L)));

        List<Favorite> actualFavorites = favoriteRepository.findAllByFavoriteIdUserId(1L);

        assertThat(actualFavorites).isEqualTo(expectedFavorites);
    }

    private void insertInitData() {
        Category category = categoryRepository.save(new Category("Category"));

        Credentials credentials = credentials().withLogin("login1").build();
        PersonalData personalData = personalData().withEmail("email1@email.ru").build();
        User user = userRepository.save(new User(credentials, personalData));

        Advertisement advertisement = new Advertisement("Advertisement1", new Date(), user);
        Product product = new Product(category, new BigDecimal(100));
        advertisement.setProduct(product);
        advertisementRepository.save(advertisement);

        credentials = credentials().withLogin("login2").build();
        personalData = personalData().withEmail("email2@email.ru").build();
        user = userRepository.save(new User(credentials, personalData));

        advertisement = new Advertisement("Advertisement2", new Date(), user);
        product = new Product(category, new BigDecimal(100));
        advertisement.setProduct(product);
        advertisementRepository.save(advertisement);
    }

}
