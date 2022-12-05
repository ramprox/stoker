package ru.stoker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.stoker.dto.advt.AdvtInfo;
import ru.stoker.dto.advt.CreateAdvt;
import ru.stoker.dto.advt.UpdateAdvt;
import ru.stoker.exceptions.Advt;
import ru.stoker.exceptions.Advt.SaveOperationException;
import ru.stoker.exceptions.AttachmentEx;
import ru.stoker.exceptions.AttachmentStorage.UnknownImageTypeException;
import ru.stoker.exceptions.Category;
import ru.stoker.service.security.StokerUserDetails;
import ru.stoker.service.advertisement.AdvertisementService;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;
import static ru.stoker.exceptions.Advt.DeleteOperationException.DELETE_OPERATION_FAILED;
import static ru.stoker.exceptions.Advt.NotFoundException.ADVT_NOT_FOUND;
import static ru.stoker.exceptions.Advt.SaveOperationException.SAVE_OPERATION_FAILED;
import static ru.stoker.exceptions.Advt.UpdateOperationException.UPDATE_OPERATION_FAILED;
import static ru.stoker.exceptions.AttachmentEx.NotFoundException.IMAGE_NOT_FOUND;
import static ru.stoker.exceptions.AttachmentStorage.UnknownImageTypeException.UNKNOWN_IMAGE_TYPE;
import static ru.stoker.exceptions.Category.NotFoundException.CATEGORY_NOT_FOUND;

@RestController
@RequestMapping("/api/v1/advertisement")
public class AdvertisementController {

    protected final AdvertisementService advertisementService;

    private final MessageSource messageSource;

    @Autowired
    public AdvertisementController(AdvertisementService advertisementService,
                                   MessageSource messageSource) {
        this.advertisementService = advertisementService;
        this.messageSource = messageSource;
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public AdvtInfo save(@RequestPart("advertisement") @Valid CreateAdvt advtDto,
                         @RequestPart(required = false) List<MultipartFile> files,
                         @AuthenticationPrincipal StokerUserDetails userDetails) {
        return advertisementService.save(userDetails.getId(), advtDto, files);
    }

    @GetMapping("/{id}")
    public AdvtInfo getById(@PathVariable Long id) {
        return advertisementService.getById(id);
    }

    @PreAuthorize("hasAuthority('USER') && @securityService.isUserHasAdvt(#advtDto.id)")
    @PutMapping(consumes = MULTIPART_FORM_DATA_VALUE)
    public AdvtInfo update(@RequestPart("advertisement") @Valid UpdateAdvt advtDto,
                          @RequestPart(required = false) List<MultipartFile> files,
                          @AuthenticationPrincipal StokerUserDetails userDetails) {
        return advertisementService.update(userDetails.getId(), advtDto, files);
    }

    @PreAuthorize("hasAuthority('USER') && @securityService.isUserHasAdvt(#id)")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id) {
        advertisementService.deleteById(id);
    }

    @GetMapping("/all")
    public List<AdvtInfo> findAll(@RequestParam Long categoryId) {
        return advertisementService.findAll(categoryId);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(Category.NotFoundException.class)
    public String handleCategoryNotFoundException(Category.NotFoundException ex, Locale locale) {
        return messageSource.getMessage(CATEGORY_NOT_FOUND, new Object[]{ex.getId()}, locale);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(Advt.NotFoundException.class)
    public String handleAdvtNotFoundException(Advt.NotFoundException ex, Locale locale) {
        return messageSource.getMessage(ADVT_NOT_FOUND, new Object[]{ ex.getId() }, locale);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(AttachmentEx.NotFoundException.class)
    public String handleAttachmentNotFoundException(AttachmentEx.NotFoundException ex, Locale locale) {
        return messageSource.getMessage(IMAGE_NOT_FOUND, new Object[]{ ex.getId() }, locale);
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(UnknownImageTypeException.class)
    public String handleUnknownImageTypeException(UnknownImageTypeException ex, Locale locale) {
        return messageSource.getMessage(UNKNOWN_IMAGE_TYPE, new Object[]{ ex.getImageType() }, locale );
    }

    @ResponseStatus(SERVICE_UNAVAILABLE)
    @ExceptionHandler(SaveOperationException.class)
    public String handleSaveOperationException(Locale locale) {
        return messageSource.getMessage(SAVE_OPERATION_FAILED,null, locale);
    }

    @ResponseStatus(SERVICE_UNAVAILABLE)
    @ExceptionHandler(Advt.UpdateOperationException.class)
    public String handleUpdateOperationException(Locale locale) {
        return messageSource.getMessage(UPDATE_OPERATION_FAILED, null, locale);
    }

    @ResponseStatus(SERVICE_UNAVAILABLE)
    @ExceptionHandler(Advt.DeleteOperationException.class)
    public String handleDeleteOperationException(Locale locale) {
        return messageSource.getMessage(DELETE_OPERATION_FAILED, null, locale );
    }

}
