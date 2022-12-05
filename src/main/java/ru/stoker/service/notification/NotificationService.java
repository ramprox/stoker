package ru.stoker.service.notification;

import ru.stoker.database.entity.embeddable.Contacts;

public interface NotificationService {

    String notify(Contacts contacts, String link);

}
