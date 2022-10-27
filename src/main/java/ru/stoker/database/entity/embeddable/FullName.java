package ru.stoker.database.entity.embeddable;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Embeddable
public class FullName {

    @NotBlank(message = "Имя не должно быть пустым")
    @Column(name = "firstname", nullable = false)
    private String firstName;

    @NotBlank(message = "Фамилия не должна быть пустой")
    @Column(name = "lastname", nullable = false)
    private String lastName;

}
