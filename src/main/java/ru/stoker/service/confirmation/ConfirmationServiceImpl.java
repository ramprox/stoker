package ru.stoker.service.confirmation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stoker.database.entity.User;
import ru.stoker.database.repository.UserRepository;
import ru.stoker.exceptions.ConfirmationEx;
import ru.stoker.properties.ConfirmProperties;
import ru.stoker.service.notification.NotificationService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class ConfirmationServiceImpl implements ConfirmationService {

    private final NotificationService notificationService;

    private final UserRepository userRepository;

    private final ConfirmProperties confirmProperties;

    @Autowired
    public ConfirmationServiceImpl(NotificationService notificationService,
                                   UserRepository userRepository,
                                   ConfirmProperties confirmProperties) {
        this.notificationService = notificationService;
        this.userRepository = userRepository;
        this.confirmProperties = confirmProperties;
    }

    @Override
    public String startConfirmFlow(User user) {
        String code = UUID.randomUUID().toString();
        String link = String.format(confirmProperties.getLinkTemplate(), user.getId(), code);
        return notificationService.notify(user.getPersonalData().getContacts(), link);
    }

    @Override
    @Transactional
    public void confirm(Long userId, String code) {
        User user = userRepository.findById(userId)
                .orElseThrow(ConfirmationEx.ConfirmTimeExpiredException::new);
        if (user.isConfirmed()) {
            throw new ConfirmationEx.UserAlreadyConfirmedException();
        }
        log.info("Пользователь с id = {} подтвердил регистрацию", userId);
        user.setConfirmed(true);
    }

    @Transactional
    @Scheduled(initialDelayString = "${confirm.checkTask.initDelay}", fixedRateString = "${confirm.checkTask.fixedRate}")
    public void confirmExpiredTask() {
        log.info("Запустилась задача на проверку подтверждения регистрации");
        List<User> notConfirmedUsers = userRepository.findByConfirmedIsFalse();
        notConfirmedUsers.forEach(user -> {
            LocalDateTime createdAt = user.getCreatedAt();
            Duration duration = Duration.between(createdAt, LocalDateTime.now());
            if(duration.compareTo(confirmProperties.getWaitDuration()) > 0) {
                userRepository.deleteById(user.getId());
                log.info("Время ожидания регистрации пользователя с id = {} истекло. Пользователь удален.", user.getId());
            }
        });
        log.info("Задача проверки подтверждения регистрации закончена");
    }

}
