package ru.stoker.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.stoker.dto.advt.AdminCreateAdvt;
import ru.stoker.dto.advt.AdminUpdateAdvt;
import ru.stoker.dto.advt.AdvtInfo;
import ru.stoker.exceptions.Advt;
import ru.stoker.exceptions.AttachmentStorage;
import ru.stoker.exceptions.Category;
import ru.stoker.exceptions.UserEx;
import ru.stoker.service.advertisement.AdminAdvtService;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
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
public class AdminAdvtController {

    private final AdminAdvtService advertisementService;

    private final MessageSource messageSource;

    @Autowired
    public AdminAdvtController(AdminAdvtService advertisementService,
                               MessageSource messageSource) {
        this.advertisementService = advertisementService;
        this.messageSource = messageSource;
    }

    @PostMapping
    public AdvtInfo create(@RequestPart("advertisement") @Valid AdminCreateAdvt advt,
                           @RequestPart(required = false) List<MultipartFile> files) {
        return advertisementService.create(advt, files);
    }

    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public AdvtInfo update(@RequestPart("advertisement") @Valid AdminUpdateAdvt advt,
                           @RequestPart(required = false) List<MultipartFile> files) {
        return advertisementService.update(advt, files);
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
