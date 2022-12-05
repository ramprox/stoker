package ru.stoker.service.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.stoker.database.entity.Advertisement;
import ru.stoker.database.repository.AdvertisementRepository;
import ru.stoker.exceptions.Advt;

import javax.validation.constraints.NotNull;

@Service("securityService")
@Validated
public class SecurityService {

    private final AdvertisementRepository advertisementRepository;

    public SecurityService(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }

    private boolean isUserInteractsWithHimself(Long id) {
        StokerUserDetails userDetails =
                (StokerUserDetails)SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();
        return userDetails.getId().equals(id);
    }

    public boolean isUserHasAdvt(@NotNull(message = "Id объявления должен быть определен") Long advtId) {
        Advertisement advt = advertisementRepository.findById(advtId)
                .orElseThrow(() -> new Advt.NotFoundException(advtId));
        Long ownerId = advt.getUser().getId();
        return isUserInteractsWithHimself(ownerId);
    }

}
