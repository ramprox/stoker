package ru.stoker.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.stoker.dto.userestimation.UpdateUserEstimation;
import ru.stoker.dto.userestimation.UserEstimationIdDto;
import ru.stoker.dto.userestimation.UserEstimationInfo;
import ru.stoker.exceptions.UserEx;
import ru.stoker.exceptions.UserEstimationEx;
import ru.stoker.service.userestimation.UserEstimationService;

import javax.validation.Valid;

import java.util.Locale;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.stoker.exceptions.UserEx.NotFoundException.USER_NOT_FOUND;
import static ru.stoker.exceptions.UserEstimationEx.AlreadyExistException.USER_ESTIMATION_EXIST;
import static ru.stoker.exceptions.UserEstimationEx.NotFoundException.USER_ESTIMATION_NOT_FOUND;
import static ru.stoker.exceptions.UserEstimationEx.SelfEstimationException.USER_SELF_ESTIMATION_UPDATE;

@PreAuthorize("hasAuthority('ADMIN')")
@RestController
@RequestMapping("/api/v1/admin/estimation/user")
public class AdminUserEstimationController {

    private final UserEstimationService userEstimationService;

    private final MessageSource messageSource;

    @Autowired
    public AdminUserEstimationController(UserEstimationService userEstimationService,
                                         MessageSource messageSource) {
        this.userEstimationService = userEstimationService;
        this.messageSource = messageSource;
    }

    @PutMapping
    public UserEstimationInfo update(@RequestBody @Valid UpdateUserEstimation userEstimationDto) {
        Long ownerId = userEstimationDto.getOwnerId();
        return userEstimationService.update(ownerId, userEstimationDto);
    }

    @DeleteMapping
    public void deleteById(@RequestBody @Valid UserEstimationIdDto dto) {
        Long ownerId = dto.getOwnerUserId();
        Long userId = dto.getUserId();
        userEstimationService.deleteByUserId(ownerId, userId);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(UserEstimationEx.SelfEstimationException.class)
    public String handleSelfEstimationException(Locale locale) {
        return messageSource.getMessage(USER_SELF_ESTIMATION_UPDATE, null, locale);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(UserEstimationEx.NotFoundException.class)
    public String handleNotFoundException(UserEstimationEx.NotFoundException ex, Locale locale) {
        return messageSource.getMessage(USER_ESTIMATION_NOT_FOUND,
                new Object[]{ ex.getOwnerUserId(), ex.getUserId() }, locale);
    }

    @ResponseStatus(CONFLICT)
    @ExceptionHandler(UserEstimationEx.AlreadyExistException.class)
    public String handleAlreadyExistException(UserEstimationEx.AlreadyExistException ex, Locale locale) {
        return messageSource.getMessage(USER_ESTIMATION_EXIST, new Object[]{ ex.getUserId() }, locale);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(UserEx.NotFoundException.class)
    public String handleAlreadyExistException(UserEx.NotFoundException ex, Locale locale) {
        return messageSource.getMessage(USER_NOT_FOUND, new Object[]{ ex.getId() }, locale);
    }

}
