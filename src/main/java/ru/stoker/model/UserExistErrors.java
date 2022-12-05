package ru.stoker.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
public class UserExistErrors {

    public static final String LOGIN_EXIST = "user.login.exist";

    public static final String EMAIL_EXIST = "user.email.exist";

    public static final String PHONE_EXIST = "user.phone.exist";

    @JsonInclude(NON_NULL)
    private String login;

    @JsonInclude(NON_NULL)
    private String email;

    @JsonInclude(NON_NULL)
    private String phone;

}
