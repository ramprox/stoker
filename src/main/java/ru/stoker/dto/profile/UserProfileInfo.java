package ru.stoker.dto.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true, exclude = "createdAt")
public class UserProfileInfo extends ProfileInfo {

    @Valid
    @NotNull(message = "{credentials.not.null}")
    private CredentialsDto credentials;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime createdAt;

}
