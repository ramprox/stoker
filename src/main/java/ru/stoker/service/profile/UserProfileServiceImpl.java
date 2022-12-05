package ru.stoker.service.profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stoker.database.entity.User;
import ru.stoker.database.repository.UserRepository;
import ru.stoker.dto.profile.CredentialsDto;
import ru.stoker.dto.profile.PersonalDataDto;
import ru.stoker.dto.profile.ProfileInfo;
import ru.stoker.dto.profile.UserProfileInfo;
import ru.stoker.exceptions.UserEx;
import ru.stoker.mapper.ProfileMapper;
import ru.stoker.service.util.UserChecker;

@Service
public class UserProfileServiceImpl implements UserProfileService {

    private final UserRepository userRepository;

    private final UserChecker userChecker;

    private final ProfileMapper profileMapper;

    @Autowired
    public UserProfileServiceImpl(UserRepository userRepository,
                                  UserChecker userChecker,
                                  ProfileMapper profileMapper) {
        this.userRepository = userRepository;
        this.userChecker = userChecker;
        this.profileMapper = profileMapper;
    }

    @Override
    public UserProfileInfo getById(Long id) {
        User user = getUserById(id);
        return profileMapper.toUserProfileInfo(user);
    }

    @Override
    public ProfileInfo findById(Long id) {
        User user = getUserById(id);
        return profileMapper.toProfileInfo(user);
    }

    @Override
    @Transactional
    public PersonalDataDto updatePersonalData(Long ownerId, PersonalDataDto userProfileInfo) {
        User user = getUserById(ownerId);
        userChecker.checkNotExistUser(userProfileInfo.getContacts(), ownerId);
        profileMapper.updateFromProfileInfo(userProfileInfo, user);
        return userProfileInfo;
    }

    @Override
    @Transactional
    public CredentialsDto updateCredentials(Long ownerId, CredentialsDto credentialsDto) {
        User user = getUserById(ownerId);
        userChecker.checkNotExistUser(credentialsDto.getLogin(), ownerId);
        profileMapper.updateFromCredentialsDto(credentialsDto, user);
        credentialsDto.setPassword(null);
        return credentialsDto;
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
