package ru.stoker.database.entity.embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class PersonalData {

    @Embedded
    private FullName fullName;

    @Embedded
    private Contacts contacts;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthDay;

}
