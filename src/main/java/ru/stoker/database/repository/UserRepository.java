package ru.stoker.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.stoker.database.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>,
        JpaSpecificationExecutor<User> {

    Optional<User> findByCredentialsLogin(String login);

    List<User> findByConfirmedIsFalse();

    Optional<User> findByConfirmCode(String confirmCode);

}
