package ru.stoker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.stoker.dto.profile.CredentialsDto;
import ru.stoker.dto.profile.PersonalDataDto;
import ru.stoker.dto.profile.ProfileInfo;
import ru.stoker.dto.profile.UserProfileInfo;
import ru.stoker.exceptions.UserEx.AlreadyExistException;
import ru.stoker.exceptions.UserEx.NotFoundException;
import ru.stoker.service.security.StokerUserDetails;
import ru.stoker.model.UserExistErrors;
import ru.stoker.service.advertisement.AdvertisementService;
import ru.stoker.service.exceptions.UserExistErrorsBuilder;
import ru.stoker.service.profile.UserProfileService;

import javax.validation.Valid;
import java.util.Locale;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.stoker.exceptions.UserEx.NotFoundException.USER_NOT_FOUND;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final UserProfileService userService;

    private final AdvertisementService advertisementService;

    private final UserExistErrorsBuilder existErrorsBuilder;

    private final MessageSource messageSource;

    @Autowired
    public ProfileController(UserProfileService userService,
                             AdvertisementService advertisementService,
                             UserExistErrorsBuilder existErrorsBuilder,
                             MessageSource messageSource) {
        this.userService = userService;
        this.advertisementService = advertisementService;
        this.existErrorsBuilder = existErrorsBuilder;
        this.messageSource = messageSource;
    }

    @GetMapping("/{id}")
    public ProfileInfo getProfileInfo(@PathVariable Long id) {
        return userService.findById(id);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping
    public UserProfileInfo getProfileInfo(@AuthenticationPrincipal StokerUserDetails userDetails) {
        return userService.getById(userDetails.getId());
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/personaldata")
    public PersonalDataDto updatePersonalData(@RequestBody @Valid PersonalDataDto user,
                                              @AuthenticationPrincipal StokerUserDetails userDetails) {
        return userService.updatePersonalData(userDetails.getId(), user);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping("/credentials")
    public CredentialsDto updateCredentials(@RequestBody @Valid CredentialsDto credentialsDto,
                                            @AuthenticationPrincipal StokerUserDetails userDetails) {
        return userService.updateCredentials(userDetails.getId(), credentialsDto);
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping
    public void deleteProfile(@AuthenticationPrincipal StokerUserDetails userDetails) {
        advertisementService.deleteAllByUserId(userDetails.getId());
        userService.deleteById(userDetails.getId());
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
