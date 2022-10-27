package ru.stoker.database.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.stoker.database.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
