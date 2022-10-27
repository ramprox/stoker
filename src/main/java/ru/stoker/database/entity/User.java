package ru.stoker.database.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.stoker.database.entity.embeddable.Credentials;
import ru.stoker.database.entity.embeddable.PersonalData;
import ru.stoker.database.entity.enums.AccountStatus;
import ru.stoker.database.entity.enums.Role;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = User.TABLE_NAME, indexes = {
        @Index(name = "login_uq", columnList = "login"),
        @Index(name = "email_uq", columnList = "email"),
        @Index(name = "phone_uq", columnList = "phone")
})
public class User {

    public static final String TABLE_NAME = "users";
    public static final String SEQUENCE_NAME = "users_seq";

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_gen")
    @SequenceGenerator(name = "seq_gen", sequenceName = "users_seq", allocationSize = 1)
    private Long id;

    @Embedded
    @Valid
    private Credentials credentials;

    @Embedded
    @Valid
    private PersonalData personalData;

    @NotNull(message = "Роль пользователя не должна быть пустой")
    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    @NotNull(message = "Статус пользователя не должен быть пустым")
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus = AccountStatus.NOT_CONFIRMED;

    public User(Credentials credentials, PersonalData personalData) {
        this.credentials = credentials;
        this.personalData = personalData;
    }
}
