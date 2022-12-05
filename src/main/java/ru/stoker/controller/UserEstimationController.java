package ru.stoker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.stoker.dto.userestimation.UserEstimationDto;
import ru.stoker.dto.userestimation.UserEstimationInfo;
import ru.stoker.exceptions.UserEx;
import ru.stoker.exceptions.UserEstimationEx;
import ru.stoker.service.security.StokerUserDetails;
import ru.stoker.service.userestimation.UserEstimationService;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpStatus.*;
import static ru.stoker.exceptions.UserEx.NotFoundException.USER_NOT_FOUND;
import static ru.stoker.exceptions.UserEstimationEx.AlreadyExistException.USER_ESTIMATION_EXIST;
import static ru.stoker.exceptions.UserEstimationEx.NotFoundException.USER_ESTIMATION_NOT_FOUND;
import static ru.stoker.exceptions.UserEstimationEx.SelfEstimationException.USER_SELF_ESTIMATION;

@RestController
@RequestMapping("/api/v1/estimation/user")
public class UserEstimationController {

    private final UserEstimationService userEstimationService;

    private final MessageSource messageSource;

    @Autowired
    public UserEstimationController(UserEstimationService userEstimationService,
                                    MessageSource messageSource) {
        this.userEstimationService = userEstimationService;
        this.messageSource = messageSource;
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping
    public UserEstimationInfo estimate(@RequestBody @Valid UserEstimationDto userEstimationDto,
                                       @AuthenticationPrincipal StokerUserDetails userDetails) {
        return userEstimationService.estimate(userDetails.getId(), userEstimationDto);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PutMapping
    public UserEstimationInfo update(@RequestBody @Valid UserEstimationDto userEstimationDto,
                                     @AuthenticationPrincipal StokerUserDetails userDetails) {
        return userEstimationService.update(userDetails.getId(), userEstimationDto);
    }

    @GetMapping("/{userId}/{ownerId}")
    public UserEstimationInfo getByEstimationId(@PathVariable Long userId, @PathVariable Long ownerId) {
        return userEstimationService.getById(ownerId, userId);
    }

    @GetMapping
    public List<UserEstimationInfo> getAllByUserId(@RequestParam Long userId) {
        return userEstimationService.getAllByUserId(userId);
    }

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping("/{userId}")
    public void deleteByUserId(@PathVariable Long userId,
                               @AuthenticationPrincipal StokerUserDetails userDetails) {
        userEstimationService.deleteByUserId(userDetails.getId(), userId);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(UserEstimationEx.SelfEstimationException.class)
    public String handleSelfEstimationException(UserEstimationEx.SelfEstimationException ex, Locale locale) {
        return messageSource.getMessage(USER_SELF_ESTIMATION, null, locale);
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
