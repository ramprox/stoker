package ru.stoker.database.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.stoker.database.entity.embeddable.Credentials;
import ru.stoker.database.entity.embeddable.PersonalData;
import ru.stoker.database.entity.enums.Role;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = User.TABLE, uniqueConstraints = {
        @UniqueConstraint(name = "login_uq", columnNames = "login"),
        @UniqueConstraint(name = "email_uq", columnNames = "email"),
        @UniqueConstraint(name = "phone_uq", columnNames = "phone")
})
public class User {

    public static final String TABLE = "users";

    public static final String SEQUENCE_NAME = "users_seq";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq_gen")
    @SequenceGenerator(name = "user_seq_gen", sequenceName = SEQUENCE_NAME, allocationSize = 1)
    private Long id;

    @Embedded
    private Credentials credentials;

    @Embedded
    private PersonalData personalData;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role = Role.USER;

    @Column(name = "is_confirmed", nullable = false)
    private boolean confirmed;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public User(Credentials credentials, PersonalData personalData) {
        this.credentials = credentials;
        this.personalData = personalData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User other)) return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
