package ru.stoker.service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.stoker.database.entity.User;
import ru.stoker.database.entity.embeddable.Contacts;
import ru.stoker.database.repository.UserRepository;
import ru.stoker.dto.profile.ContactsDto;
import ru.stoker.exceptions.UserEx;
import ru.stoker.model.UserCheckingFieldValues;
import ru.stoker.model.UserExistErrors;

import java.util.List;

import static ru.stoker.database.specification.UserSpecification.*;

@Service
public class UserChecker {

    private final UserRepository userRepository;

    @Autowired
    public UserChecker(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void checkNotExistUser(String login, ContactsDto contactsDto) {
        Specification<User> spec = byLoginOrContacts(login, contactsDto);
        UserCheckingFieldValues checkingFieldValues = getCheckingValues(login, contactsDto);
        checkUserExist(checkingFieldValues, spec);
    }

    public void checkNotExistUser(String login, ContactsDto contactsDto, Long excludeId) {
        Specification<User> spec = byLoginOrContactsExcludeId(login, contactsDto, excludeId);
        UserCheckingFieldValues checkingFieldValues = getCheckingValues(login, contactsDto);
        checkUserExist(checkingFieldValues, spec);
    }

    public void checkNotExistUser(String login, Long excludedUserId) {
        Specification<User> specification = byLoginExcludeId(login, excludedUserId);
        UserCheckingFieldValues checkingFieldValues = getCheckingValues(login);
        checkUserExist(checkingFieldValues, specification);
    }

    public void checkNotExistUser(ContactsDto contactsDto, Long excludedUserId) {
        Specification<User> specification = byEmailOrPhoneExcludeId(contactsDto, excludedUserId);
        UserCheckingFieldValues checkingFieldValues = getCheckingValues(contactsDto);
        checkUserExist(checkingFieldValues, specification);
    }

    private UserCheckingFieldValues getCheckingValues(String login) {
        UserCheckingFieldValues checkingFieldValues = new UserCheckingFieldValues();
        checkingFieldValues.setLogin(login);
        return checkingFieldValues;
    }

    private UserCheckingFieldValues getCheckingValues(ContactsDto contactsDto) {
        UserCheckingFieldValues checkingFieldValues = new UserCheckingFieldValues();
        checkingFieldValues.setEmail(contactsDto.getEmail());
        checkingFieldValues.setPhone(contactsDto.getPhone());
        return checkingFieldValues;
    }

    private UserCheckingFieldValues getCheckingValues(String login, ContactsDto contactsDto) {
        UserCheckingFieldValues checkingFieldValues = new UserCheckingFieldValues();
        checkingFieldValues.setLogin(login);
        checkingFieldValues.setEmail(contactsDto.getEmail());
        checkingFieldValues.setPhone(contactsDto.getPhone());
        return checkingFieldValues;
    }

    private void checkUserExist(UserCheckingFieldValues uniqueFields, Specification<User> spec) {
        List<User> existedUsers = userRepository.findAll(spec);
        if(existedUsers.size() > 0) {
            UserExistErrors existErrors = buildUserExistErrors(existedUsers, uniqueFields);
            throw new UserEx.AlreadyExistException(existErrors);
        }
    }

    private UserExistErrors buildUserExistErrors(List<User> existedUsers, UserCheckingFieldValues uniqueFields) {
        String login = uniqueFields.getLogin();
        String email = uniqueFields.getEmail();
        String phone = uniqueFields.getPhone();
        UserExistErrors errors = new UserExistErrors();
        existedUsers.forEach(user -> {
            Contacts contacts = user.getPersonalData().getContacts();
            String existedEmail = contacts.getEmail();
            String existedPhone = contacts.getPhone();
            String existedLogin = user.getCredentials().getLogin();
            if(existedLogin.equals(login)) {
                errors.setLogin(login);
            }
            if(existedEmail.equals(email)) {
                errors.setEmail(email);
            }
            if(existedPhone.equals(phone)) {
                errors.setPhone(phone);
            }
        });
        return errors;
    }

}
