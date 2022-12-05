package ru.stoker.service.profile;

import ru.stoker.dto.profile.*;
import ru.stoker.dto.profile.ProfileInfo;
import ru.stoker.dto.profile.UserProfileInfo;

public interface UserProfileService {

    ProfileInfo findById(Long id);

    UserProfileInfo getById(Long id);

    PersonalDataDto updatePersonalData(Long ownerId, PersonalDataDto userDto);

    CredentialsDto updateCredentials(Long ownerId, CredentialsDto credentialsDto);

    void deleteById(Long id);

}
