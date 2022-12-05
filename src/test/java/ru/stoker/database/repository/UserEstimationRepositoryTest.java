package ru.stoker.database.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.stoker.anotations.RepositoryIT;
import ru.stoker.database.entity.User;
import ru.stoker.database.entity.UserEstimation;
import ru.stoker.database.entity.embeddable.Credentials;
import ru.stoker.database.entity.embeddable.Estimation;
import ru.stoker.database.entity.embeddable.PersonalData;
import ru.stoker.database.entity.embeddable.UserEstimationId;
import ru.stoker.util.builder.DatabaseFacade;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.stoker.util.builder.CredentialsBuilder.credentials;
import static ru.stoker.util.builder.PersonalDataBuilder.personalData;

@DisplayName("Интеграционные тесты для UserEstimationRepository")
@RepositoryIT
public class UserEstimationRepositoryTest {

    @Autowired
    private UserEstimationRepository userEstimationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DatabaseFacade databaseFacade;

    @BeforeEach
    public void beforeEach() {
        databaseFacade.clean();
        databaseFacade.executeInTransaction(this::insertInitData);
    }

    @DisplayName("Сохранение оценки пользователя")
    @Test
    public void saveUserEstimationTest() {
        List<UserEstimationId> expectedUserEstimationIds = userEstimationRepository.saveAll(
                List.of(new UserEstimation(new UserEstimationId(1L, 2L),
                                new Estimation(5, LocalDateTime.now())),
                        new UserEstimation(new UserEstimationId(1L, 3L),
                                new Estimation(4, LocalDateTime.now())))
        ).stream().map(UserEstimation::getId).toList();

        databaseFacade.executeInTransaction(() -> {
            List<UserEstimation> actualUserEstimations = userEstimationRepository.findAllById(
                    List.of(new UserEstimationId(1L, 2L),
                            new UserEstimationId(1L, 3L))
            );

            assertThat(actualUserEstimations)
                    .extracting(UserEstimation::getId)
                    .containsExactlyInAnyOrderElementsOf(expectedUserEstimationIds);
        });
    }

    @DisplayName("Извлечение оценок пользователя")
    @Test
    public void findUserEstimationsByEstimatedUserIdTest() {
        List<UserEstimationId> expectedUserEstimationIds = userEstimationRepository.saveAll(
                List.of(new UserEstimation(new UserEstimationId(1L, 2L),
                                new Estimation(5, LocalDateTime.now())),
                        new UserEstimation(new UserEstimationId(3L, 2L),
                                new Estimation(4, LocalDateTime.now())))
        ).stream().map(UserEstimation::getId).toList();

        databaseFacade.executeInTransaction(() -> {
            List<UserEstimation> actualUserEstimations = userEstimationRepository
                    .findByIdUserId(2L);

            assertThat(actualUserEstimations)
                    .extracting(UserEstimation::getId)
                    .containsExactlyInAnyOrderElementsOf(expectedUserEstimationIds);
        });
    }

    private void insertInitData() {
        Credentials credentials = credentials().withLogin("login1").build();
        PersonalData personalData = personalData().withEmail("email1@email.ru")
                .withPhone("89999999991").build();
        User user = new User(credentials, personalData);
        user.setCreatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        credentials = credentials().withLogin("login2").build();
        personalData = personalData().withEmail("email2@email.ru")
                .withPhone("89999999992").build();
        user = new User(credentials, personalData);
        user.setCreatedAt(LocalDateTime.now());
        user = userRepository.save(user);

        credentials = credentials().withLogin("login3").build();
        personalData = personalData().withEmail("email3@email.ru")
                .withPhone("89999999993").build();
        user = new User(credentials, personalData);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

    }

}
