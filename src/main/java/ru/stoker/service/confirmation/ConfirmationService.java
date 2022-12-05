package ru.stoker.service.confirmation;

import ru.stoker.database.entity.User;

public interface ConfirmationService {

    String startConfirmFlow(User user);

    void confirm(Long userId, String code);

}
