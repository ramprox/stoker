package ru.stoker.dto.profile;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class FullNameDto {

    @NotBlank(message = "{firstName.not.blank}")
    private String firstName;

    @NotBlank(message = "{lastName.not.blank}")
    private String lastName;

}
