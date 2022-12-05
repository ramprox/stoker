package ru.stoker.dto.profile;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@EqualsAndHashCode(exclude = "password")
public class CredentialsDto {

    @NotBlank(message = "{login.not.blank}")
    private String login;

    @JsonInclude(NON_NULL)
    @NotBlank(message = "{password.not.blank}")
    private String password;

}
