package ru.stoker.util;

import ru.stoker.database.entity.embeddable.FullName;
import ru.stoker.database.entity.embeddable.PersonalData;

import java.time.LocalDate;
import java.util.Date;

public class PersonalDataBuilder implements EntityBuilder<PersonalData>{

    private String email = "email@email.ru";

    private FullName fullName = new FullName("firstName", "lastName");

    private Date birthDay = new Date(LocalDate.of(2000, 1, 1).toEpochDay());

    private String phone = "8-999-999-99-99";

    public static PersonalDataBuilder personalData() {
        return new PersonalDataBuilder();
    }

    private PersonalDataBuilder() { }

    private PersonalDataBuilder(PersonalDataBuilder builder) {
        this.email = builder.email;
        this.fullName = builder.fullName;
        this.birthDay = builder.birthDay;
        this.phone = builder.phone;
    }

    public PersonalDataBuilder withEmail(String email) {
        PersonalDataBuilder builder = new PersonalDataBuilder(this);
        builder.email = email;
        return builder;
    }

    public PersonalDataBuilder withFullName(FullName fullName) {
        PersonalDataBuilder builder = new PersonalDataBuilder(this);
        builder.fullName = fullName;
        return builder;
    }

    public PersonalDataBuilder withBirthDay(Date birthDay) {
        PersonalDataBuilder builder = new PersonalDataBuilder(this);
        builder.birthDay = birthDay;
        return builder;
    }

    public PersonalDataBuilder withPhone(String phone) {
        PersonalDataBuilder builder = new PersonalDataBuilder(this);
        builder.phone = phone;
        return builder;
    }

    @Override
    public PersonalData build() {
        return new PersonalData(email, fullName, birthDay, phone);
    }
}
