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
public class Address {

    @NotBlank(message = "Название страны не должно быть пустым")
    @Column(name = "country", nullable = false)
    private String country;

    @NotBlank(message = "Название города не должно быть пустым")
    @Column(name = "city", nullable = false)
    private String city;

    @NotBlank(message = "Название улицы не должно быть пустым")
    @Column(name = "street", nullable = false)
    private String street;

    @NotBlank(message = "Номер дома не должен быть пустым")
    @Column(name = "home_number", nullable = false)
    private String homeNumber;

}
