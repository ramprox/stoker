package ru.stoker.database.repository;

import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import ru.stoker.anotations.RepositoryIT;
import ru.stoker.database.entity.User;
import ru.stoker.database.entity.embeddable.Credentials;
import ru.stoker.database.entity.embeddable.PersonalData;
import ru.stoker.util.builder.DatabaseFacade;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.stoker.util.builder.CredentialsBuilder.credentials;
import static ru.stoker.util.builder.PersonalDataBuilder.personalData;

@DisplayName("Интеграционные тесты для UserRepository")
@RepositoryIT
public class UserRepositoryTest {

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
        User user = new User(credentials, personalData);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        Optional<User> actualUserOpt = userRepository.findById(1L);
        assertThat(actualUserOpt.isPresent()).isEqualTo(true);
    }

    @DisplayName("Сохранение пользователя с уже существующим логином")
    @Test
    public void saveWithTheSameLoginTest() {
        Credentials credentials = credentials().build();
        PersonalData personalData = personalData().build();
        User user = new User(credentials, personalData);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            PersonalData newPersonalData = personalData().withEmail("email1@email.ru")
                    .withPhone("89999999992").build();
            User user1 = new User(credentials, newPersonalData);
            user1.setCreatedAt(LocalDateTime.now());
            userRepository.save(user1);
        });
        assertEqualDetail(exception, "Key (login)=(login) already exists.");
    }

    @DisplayName("Сохранение пользователя с уже существующим email")
    @Test
    public void saveWithTheSameEmailTest() {
        Credentials credentials = credentials().build();
        PersonalData personalData = personalData().build();
        User user = new User(credentials, personalData);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            Credentials newCredentials = credentials().withLogin("login1").build();
            PersonalData newPersonalData = personalData()
                    .withPhone("89999999992").build();
            User user1 = new User(newCredentials, newPersonalData);
            user1.setCreatedAt(LocalDateTime.now());
            userRepository.save(user1);
        });
        assertEqualDetail(exception, "Key (email)=(email@mail.ru) already exists.");
    }

    @DisplayName("Сохранение пользователя с уже существующим phone")
    @Test
    public void saveWithTheSamePhoneTest() {
        Credentials credentials = credentials().build();
        PersonalData personalData = personalData().build();
        User user = new User(credentials, personalData);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, () -> {
            Credentials newCredentials = credentials().withLogin("login1").build();
            PersonalData newPersonalData = personalData()
                    .withEmail("email1@email.ru").build();
            User user1 = new User(newCredentials, newPersonalData);
            user1.setCreatedAt(LocalDateTime.now());
            userRepository.save(user1);
        });
        assertEqualDetail(exception, "Key (phone)=(89999999999) already exists.");
    }

    private void assertEqualDetail(DataIntegrityViolationException exception, String detail) {
        ConstraintViolationException violationException = (ConstraintViolationException) exception.getCause();
        PSQLException psqlException = (PSQLException) violationException.getCause();
        assertThat(psqlException.getServerErrorMessage().getDetail()).isEqualTo(detail);
    }

}
