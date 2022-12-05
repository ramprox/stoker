package ru.stoker.service.profile;

import ru.stoker.dto.profile.*;
import ru.stoker.dto.profile.AdminUserProfileInfo;

import java.util.List;

public interface AdminProfileService {

    List<AdminUserProfileInfo> getAll();

    AdminUserProfileInfo findById(Long id);

    AdminUserProfileInfo save(AdminUserProfileInfo user);

    AdminUserProfileInfo updateProfile(AdminUserProfileInfo userDto);

    void deleteById(Long id);

}
