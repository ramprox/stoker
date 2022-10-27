package ru.stoker.database.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.stoker.annotations.RepositoryIT;
import ru.stoker.database.entity.User;
import ru.stoker.database.entity.UserEstimation;
import ru.stoker.database.entity.embeddable.*;
import ru.stoker.util.DatabaseFacade;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.stoker.util.CredentialsBuilder.credentials;
import static ru.stoker.util.PersonalDataBuilder.personalData;

@DisplayName("Интеграционные тесты для UserEstimationRepository")
@RepositoryIT
public class UserEstimationRepositoryIT {

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
                List.of(new UserEstimation(1L, 2L,
                                new Estimation(5, new Date())),
                        new UserEstimation(1L, 3L,
                                new Estimation(4, new Date())))
        ).stream().map(UserEstimation::getUserEstimationId).toList();

        List<UserEstimation> actualUserEstimations = userEstimationRepository.findAllById(
                List.of(new UserEstimationId(1L, 2L),
                        new UserEstimationId(1L, 3L))
        );

        assertThat(actualUserEstimations)
                .extracting(UserEstimation::getUserEstimationId)
                .isEqualTo(expectedUserEstimationIds);
    }

    @DisplayName("Извлечение оценок пользователя")
    @Test
    public void findUserEstimationsByEstimatedUserIdTest() {
        List<UserEstimationId> expectedUserEstimationIds = userEstimationRepository.saveAll(
                List.of(new UserEstimation(1L, 2L,
                                new Estimation(5, new Date())),
                        new UserEstimation(3L, 2L,
                                new Estimation(4, new Date())))
        ).stream().map(UserEstimation::getUserEstimationId).toList();

        List<UserEstimation> actualUserEstimations = userEstimationRepository
                .findAllByUserEstimationIdEstimatedUserId(2L);

        assertThat(actualUserEstimations)
                .extracting(UserEstimation::getUserEstimationId)
                .isEqualTo(expectedUserEstimationIds);
    }

    private void insertInitData() {
        Credentials credentials = credentials().withLogin("login1").build();
        PersonalData personalData = personalData().withEmail("email1@email.ru").build();
        userRepository.save(new User(credentials, personalData));

        credentials = credentials().withLogin("login2").build();
        personalData = personalData().withEmail("email2@email.ru").build();
        userRepository.save(new User(credentials, personalData));

        credentials = credentials().withLogin("login3").build();
        personalData = personalData().withEmail("email3@email.ru").build();
        userRepository.save(new User(credentials, personalData));

    }

}
