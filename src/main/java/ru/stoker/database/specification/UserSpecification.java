package ru.stoker.database.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.stoker.database.entity.User;
import ru.stoker.dto.profile.ContactsDto;

public class UserSpecification {

    public static Specification<User> byLoginOrContacts(String login, ContactsDto contactsDto) {
        return (root, query, builder) ->
                byLogin(login)
                        .or(byEmailOrPhone(contactsDto))
                        .toPredicate(root, query, builder);
    }

    public static Specification<User> byLoginOrContactsExcludeId(String login, ContactsDto contactsDto, Long id) {
        return (root, query, builder) ->
                byLogin(login)
                        .or(byEmailOrPhone(contactsDto))
                        .and(notId(id))
                        .toPredicate(root, query, builder);
    }

    public static Specification<User> byLoginExcludeId(String login, Long id) {
        return (root, query, builder) ->
                byLogin(login).and(notId(id))
                        .toPredicate(root, query, builder);
    }

    public static Specification<User> byEmailOrPhoneExcludeId(ContactsDto contactsDto, Long id) {
        return (root, query, builder) ->
                byEmailOrPhone(contactsDto)
                        .and(notId(id))
                        .toPredicate(root, query, builder);
    }

    private static Specification<User> notId(Long id) {
        return (root, query, builder) -> builder.notEqual(root.get("id"), id);
    }

    private static Specification<User> byLogin(String login) {
        return (root, query, builder) -> builder.equal(root.get("credentials").get("login"), login);
    }

    private static Specification<User> byEmail(String email) {
        return (root, query, builder) -> builder.equal(root.get("personalData").get("contacts").get("email"), email);
    }

    private static Specification<User> byPhone(String phone) {
        return (root, query, builder) -> builder.equal(root.get("personalData").get("contacts").get("phone"), phone);
    }

    private static Specification<User> byEmailOrPhone(ContactsDto contactsDto) {
        return (root, query, builder) ->
                byEmail(contactsDto.getEmail())
                        .or(byPhone(contactsDto.getPhone()))
                        .toPredicate(root, query, builder);
    }

}
