package ru.stoker.service.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stoker.database.entity.User;
import ru.stoker.database.repository.UserRepository;
import ru.stoker.dto.profile.AdminUserProfileInfo;
import ru.stoker.dto.profile.ContactsDto;
import ru.stoker.exceptions.UserEx;
import ru.stoker.mapper.ProfileMapper;
import ru.stoker.service.util.UserChecker;

import java.util.List;

@Service
public class AdminProfileServiceImpl implements AdminProfileService {

    private final UserRepository userRepository;

    private final UserChecker userChecker;

    private final ProfileMapper profileMapper;

    @Autowired
    public AdminProfileServiceImpl(UserRepository userRepository,
                                   UserChecker userChecker,
                                   ProfileMapper adminProfileMapper) {
        this.userRepository = userRepository;
        this.userChecker = userChecker;
        this.profileMapper = adminProfileMapper;
    }

    @Override
    public List<AdminUserProfileInfo> getAll() {
        return profileMapper.toListProfileInfo(userRepository.findAll());
    }

    @Override
    public AdminUserProfileInfo findById(Long id) {
        User user = getUserById(id);
        return profileMapper.toAdminProfileInfo(user);
    }

    @Override
    public AdminUserProfileInfo save(AdminUserProfileInfo createProfileReq) {
        String login = createProfileReq.getCredentials().getLogin();
        ContactsDto contactsDto = createProfileReq.getPersonalData().getContacts();
        userChecker.checkNotExistUser(login, contactsDto);
        User user = profileMapper.fromProfileInfo(createProfileReq);
        return profileMapper.toAdminProfileInfo(userRepository.save(user));
    }

    @Override
    @Transactional
    public AdminUserProfileInfo updateProfile(AdminUserProfileInfo adminUserProfileInfo) {
        Long userId = adminUserProfileInfo.getId();
        User user = getUserById(userId);
        String login = adminUserProfileInfo.getCredentials().getLogin();
        ContactsDto contactsDto = adminUserProfileInfo.getPersonalData().getContacts();
        userChecker.checkNotExistUser(login, contactsDto, userId);
        profileMapper.updateFromProfileInfo(adminUserProfileInfo, user);
        return profileMapper.toAdminProfileInfo(user);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserEx.NotFoundException(id));
    }

}
