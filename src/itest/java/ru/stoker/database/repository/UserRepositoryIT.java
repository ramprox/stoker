package ru.stoker.database.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.stoker.annotations.RepositoryIT;
import ru.stoker.database.entity.User;
import ru.stoker.database.entity.embeddable.Credentials;
import ru.stoker.database.entity.embeddable.PersonalData;
import ru.stoker.util.DatabaseFacade;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.stoker.util.CredentialsBuilder.credentials;
import static ru.stoker.util.PersonalDataBuilder.personalData;

@DisplayName("Интеграционные тесты для UserRepository")
@RepositoryIT
public class UserRepositoryIT {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DatabaseFacade databaseFacade;

    @BeforeEach
    public void beforeEach() {
        databaseFacade.clean();
    }

    @DisplayName("Сохранение пользователя")
    @Test
    public void saveTest() {
        Credentials credentials = credentials().build();
        PersonalData personalData = personalData().build();

        userRepository.save(new User(credentials, personalData));

        Optional<User> actualUserOpt = userRepository.findById(1L);
        assertThat(actualUserOpt.isPresent()).isEqualTo(true);
    }

}
