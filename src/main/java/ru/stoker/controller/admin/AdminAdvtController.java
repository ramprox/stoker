package ru.stoker.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.stoker.dto.advt.AdminCreateAdvt;
import ru.stoker.dto.advt.AdminUpdateAdvt;
import ru.stoker.dto.advt.AdvtInfo;
import ru.stoker.exceptions.Advt;
import ru.stoker.exceptions.AttachmentStorage;
import ru.stoker.exceptions.Category;
import ru.stoker.exceptions.UserEx;
import ru.stoker.service.advertisement.AdminAdvtService;

import javax.validation.Valid;
import java.util.Locale;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE;
import static ru.stoker.exceptions.Advt.DeleteOperationException.DELETE_OPERATION_FAILED;
import static ru.stoker.exceptions.Advt.NotFoundException.ADVT_NOT_FOUND;
import static ru.stoker.exceptions.Advt.SaveOperationException.SAVE_OPERATION_FAILED;
import static ru.stoker.exceptions.Advt.UpdateOperationException.UPDATE_OPERATION_FAILED;
import static ru.stoker.exceptions.AttachmentStorage.UnknownImageTypeException.UNKNOWN_IMAGE_TYPE;
import static ru.stoker.exceptions.Category.NotFoundException.CATEGORY_NOT_FOUND;
import static ru.stoker.exceptions.UserEx.NotFoundException.USER_NOT_FOUND;

@PreAuthorize("hasAuthority('ADMIN')")
@RestController
@RequestMapping("/api/v1/admin/advertisement")
@RequiredArgsConstructor
public class AdminAdvtController {

    private final AdminAdvtService advertisementService;

    private final MessageSource messageSource;

    @PostMapping
    public AdvtInfo create(@RequestBody @Valid AdminCreateAdvt advt) {
        return advertisementService.create(advt);
    }

    @PutMapping
    public AdvtInfo update(@RequestBody @Valid AdminUpdateAdvt advt) {
        return advertisementService.update(advt);
    }

    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        advertisementService.deleteById(id);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(Category.NotFoundException.class)
    public String handleCategoryNotFoundException(Category.NotFoundException ex, Locale locale) {
        return messageSource.getMessage(CATEGORY_NOT_FOUND, new Object[]{ ex.getId() }, locale );
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(Advt.NotFoundException.class)
    public String handleAdvertisementNotFoundException(Advt.NotFoundException ex, Locale locale) {
        return messageSource.getMessage(ADVT_NOT_FOUND, new Object[]{ ex.getId() }, locale );
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(UserEx.NotFoundException.class)
    public String handleUserNotFoundException(UserEx.NotFoundException ex, Locale locale) {
        return messageSource.getMessage(USER_NOT_FOUND, new Object[]{ ex.getId() }, locale );
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(AttachmentStorage.UnknownImageTypeException.class)
    public String handleUnknownImageTypeException(AttachmentStorage.UnknownImageTypeException ex, Locale locale) {
        return messageSource.getMessage(UNKNOWN_IMAGE_TYPE, new Object[]{ ex.getImageType() }, locale );
    }

    @ResponseStatus(SERVICE_UNAVAILABLE)
    @ExceptionHandler(Advt.SaveOperationException.class)
    public String handleSaveOperationException(Locale locale) {
        return messageSource.getMessage(SAVE_OPERATION_FAILED, null, locale);
    }

    @ResponseStatus(SERVICE_UNAVAILABLE)
    @ExceptionHandler(Advt.UpdateOperationException.class)
    public String handleUpdateOperationException(Locale locale) {
        return messageSource.getMessage(UPDATE_OPERATION_FAILED, null, locale);
    }

    @ResponseStatus(SERVICE_UNAVAILABLE)
    @ExceptionHandler(Advt.DeleteOperationException.class)
    public String handleDeleteOperationException(Locale locale) {
        return messageSource.getMessage(DELETE_OPERATION_FAILED, null, locale);
    }

}
