package ru.stoker.util.factory;

import ru.stoker.database.entity.enums.Role;
import ru.stoker.dto.profile.AdminUserProfileInfo;
import ru.stoker.dto.profile.CredentialsDto;
import ru.stoker.dto.profile.PersonalDataDto;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static ru.stoker.util.builder.CredentialsBuilder.credentials;
import static ru.stoker.util.builder.PersonalDataBuilder.personalData;

public class AdminProfileFactory {

    public AdminUserProfileInfo defaultProfileInfo() {
        AdminUserProfileInfo profileInfo = new AdminUserProfileInfo();
        CredentialsDto credentialsDto = credentials().buildDto();
        PersonalDataDto personalDataDto = personalData().buildDto();
        profileInfo.setCredentials(credentialsDto);
        profileInfo.setPersonalData(personalDataDto);
        profileInfo.setRole(Role.USER);
        profileInfo.setConfirmed(true);
        profileInfo.setConfirmCode(UUID.randomUUID().toString());
        return profileInfo;
    }

    public List<AdminUserProfileInfo> defaultProfiles(int count) {
        return IntStream.range(0, count)
                .mapToObj(i -> {
                    String login = "login" + i;
                    String email = "email" + i + "@mail.ru";
                    String phone = "8222222222" + i;
                    return profile(login, email, phone);
                }).collect(Collectors.toList());
    }

    private AdminUserProfileInfo profile(String login, String email, String phone) {
        AdminUserProfileInfo profileInfo = new AdminUserProfileInfo();
        CredentialsDto credentialsDto = credentials().withLogin(login).buildDto();
        PersonalDataDto personalDataDto = personalData().withEmail(email).withPhone(phone).buildDto();
        profileInfo.setCredentials(credentialsDto);
        profileInfo.setPersonalData(personalDataDto);
        profileInfo.setRole(Role.USER);
        profileInfo.setConfirmed(true);
        profileInfo.setConfirmCode(UUID.randomUUID().toString());
        return profileInfo;
    }

}
