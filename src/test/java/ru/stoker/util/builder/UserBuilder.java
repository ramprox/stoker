package ru.stoker.util.builder;

import ru.stoker.database.entity.User;
import ru.stoker.database.entity.enums.Role;

import java.time.LocalDateTime;

import static ru.stoker.util.builder.CredentialsBuilder.credentials;
import static ru.stoker.util.builder.PersonalDataBuilder.personalData;

public class UserBuilder implements EntityBuilder<User> {

    private Long id;

    private CredentialsBuilder credentials = credentials();

    private PersonalDataBuilder personalData = personalData();

    private Role role = Role.USER;

    private boolean confirmed = true;

    private LocalDateTime createdAt = LocalDateTime.now();

    private UserBuilder() {}

    public static UserBuilder user() {
        return new UserBuilder();
    }

    private UserBuilder(UserBuilder builder) {
        this.id = builder.id;
        this.credentials = builder.credentials;
        this.personalData = builder.personalData;
        this.role = builder.role;
        this.confirmed = builder.confirmed;
        this.createdAt = builder.createdAt;
    }

    public UserBuilder withId(Long id) {
        UserBuilder builder = new UserBuilder(this);
        builder.id = id;
        return builder;
    }

    public UserBuilder withCredentials(CredentialsBuilder credentials) {
        UserBuilder builder = new UserBuilder(this);
        builder.credentials = credentials;
        return builder;
    }

    public UserBuilder withPersonalData(PersonalDataBuilder personalData) {
        UserBuilder builder = new UserBuilder(this);
        builder.personalData = personalData;
        return builder;
    }

    public UserBuilder withRole(Role role) {
        UserBuilder builder = new UserBuilder(this);
        builder.role = role;
        return builder;
    }

    public UserBuilder withConfirmed(boolean confirmed) {
        UserBuilder builder = new UserBuilder(this);
        builder.confirmed = confirmed;
        return builder;
    }

    public UserBuilder withCreatedAt(LocalDateTime createdAt) {
        UserBuilder builder = new UserBuilder(this);
        builder.createdAt = createdAt;
        return builder;
    }

    @Override
    public User build() {
        User user = new User();
        user.setId(id);
        user.setCredentials(credentials.build());
        user.setPersonalData(personalData.build());
        user.setRole(role);
        user.setConfirmed(confirmed);
        user.setCreatedAt(createdAt);
        return user;
    }
}
