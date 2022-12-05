package ru.stoker.dto.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class PersonalDataDto {

    @Valid
    @NotNull(message = "{fullName.not.null}")
    private FullNameDto fullName;

    @Valid
    @NotNull(message = "{contacts.not.null}")
    private ContactsDto contacts;

    @NotNull(message = "{birthday.not.null}")
    @Past(message = "{birthday.past}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate birthDay;

}
