package ru.stoker.util.builder;

import ru.stoker.database.entity.embeddable.Contacts;
import ru.stoker.database.entity.embeddable.FullName;
import ru.stoker.database.entity.embeddable.PersonalData;
import ru.stoker.dto.profile.ContactsDto;
import ru.stoker.dto.profile.FullNameDto;
import ru.stoker.dto.profile.PersonalDataDto;

import java.time.LocalDate;

public class PersonalDataBuilder implements EntityBuilder<PersonalData>{

    private String email = "email@mail.ru";

    private FullName fullName = new FullName("firstName", "lastName");

    private LocalDate birthDay = LocalDate.of(2000, 1, 1);

    private String phone = "89999999999";

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

    public PersonalDataBuilder withBirthDay(LocalDate birthDay) {
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
        Contacts contacts = new Contacts();
        contacts.setEmail(email);
        contacts.setPhone(phone);
        PersonalData personalData = new PersonalData();
        personalData.setFullName(fullName);
        personalData.setContacts(contacts);
        personalData.setBirthDay(birthDay);
        return personalData;
    }

    public PersonalDataDto buildDto() {
        FullNameDto fullNameDto = new FullNameDto();
        fullNameDto.setFirstName(fullName.getFirstName());
        fullNameDto.setLastName(fullName.getLastName());
        PersonalDataDto personalDataDto = new PersonalDataDto();
        personalDataDto.setFullName(fullNameDto);
        ContactsDto contactsDto = new ContactsDto();
        contactsDto.setEmail(email);
        contactsDto.setPhone(phone);
        personalDataDto.setBirthDay(birthDay);
        personalDataDto.setContacts(contactsDto);
        return personalDataDto;
    }

}
