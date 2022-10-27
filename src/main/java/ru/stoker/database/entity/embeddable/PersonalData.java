package ru.stoker.database.entity.embeddable;

import lombok.*;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class PersonalData {

    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
            message = "Email должен быть валидным")
    @Column(name = "email", nullable = false)
    private String email;

    @Embedded
    @Valid
    private FullName fullName;

    @NotNull(message = "Дата рождения не должна быть пустой")
    @Past(message = "Дата рождения должна быть в прошедшем времени")
    @Temporal(TemporalType.DATE)
    @Column(name = "birthday", nullable = false)
    private Date birthDay;

    @Pattern(regexp = "^(\\+?\\d{1,3}()?)?[- ]?(\\(\\d{3}\\)|\\d{3})[- .]?\\d{3}[- .]?\\d{2}[- ]?\\d{2}$",
            message = "Номер телефона должен быть валидным")
    @Column(name = "phone", nullable = false, length = 20)
    private String phone;

}
