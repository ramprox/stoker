package ru.stoker.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.stoker.dto.profile.AdminUserProfileInfo;
import ru.stoker.dto.util.validgroups.OnCreate;
import ru.stoker.dto.util.validgroups.OnUpdate;
import ru.stoker.exceptions.UserEx.AlreadyExistException;
import ru.stoker.exceptions.UserEx.NotFoundException;
import ru.stoker.model.UserExistErrors;
import ru.stoker.service.profile.AdminProfileService;
import ru.stoker.service.advertisement.AdvertisementService;
import ru.stoker.service.exceptions.UserExistErrorsBuilder;

import javax.validation.groups.Default;
import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.stoker.exceptions.UserEx.NotFoundException.USER_NOT_FOUND;

@PreAuthorize("hasAuthority('ADMIN')")
@RestController
@RequestMapping("/api/v1/admin/profile")
public class AdminProfileController {

    private final AdminProfileService userService;

    private final AdvertisementService advertisementService;

    private final UserExistErrorsBuilder existErrorsBuilder;

    private final MessageSource messageSource;

    @Autowired
    public AdminProfileController(AdminProfileService userService,
                                  AdvertisementService advertisementService,
                                  UserExistErrorsBuilder existErrorsBuilder,
                                  MessageSource messageSource) {
        this.userService = userService;
        this.advertisementService = advertisementService;
        this.existErrorsBuilder = existErrorsBuilder;
        this.messageSource = messageSource;
    }

    @GetMapping("/all")
    public List<AdminUserProfileInfo> getAll() {
        return userService.getAll();
    }

    @GetMapping("/{id}")
    public AdminUserProfileInfo getById(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PostMapping
    public AdminUserProfileInfo save(@RequestBody
                                         @Validated({ OnCreate.class, Default.class })
                                                 AdminUserProfileInfo user) {
        return userService.save(user);
    }

    @PutMapping
    public AdminUserProfileInfo updateProfile(@RequestBody
                                                  @Validated({ OnUpdate.class, Default.class })
                                                          AdminUserProfileInfo user) {
        return userService.updateProfile(user);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        advertisementService.deleteAllByUserId(id);
        userService.deleteById(id);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    public String handleUserIdNotFoundException(NotFoundException ex, Locale locale) {
        return messageSource.getMessage(USER_NOT_FOUND, new Object[]{ ex.getId() }, locale);
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(AlreadyExistException.class)
    public UserExistErrors handleUserAlreadyExistException(AlreadyExistException ex, Locale locale) {
        return existErrorsBuilder.buildErrors(ex.getErrors(), locale);
    }

}
