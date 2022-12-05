package ru.stoker.dto.profile;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;

@Data
public class ContactsDto {

    @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",  // RFC 5322
            message = "{email.valid}")
    private String email;

    @Pattern(regexp = "\\d+", message = "{phone.isDigit}")
    @Length(min = 10, max = 20, message = "{phone.length}")
    private String phone;

}
