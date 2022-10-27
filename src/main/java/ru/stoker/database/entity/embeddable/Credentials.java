package ru.stoker.database.entity.embeddable;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class Credentials {

    @NotBlank(message = "Логин не должен быть пустым")
    @Column(name = "login", nullable = false)
    private String login;

    @NotBlank(message = "Пароль не должен быть пустым")
    @Column(name = "password", nullable = false)
    private String password;

}
