package ru.stoker.controller;

import org.springframework.context.MessageSource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.stoker.exceptions.AttachmentEx.NotFoundException;
import ru.stoker.model.AttachmentDto;
import ru.stoker.service.attachment.AttachmentService;

import java.util.Locale;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static ru.stoker.exceptions.AttachmentEx.NotFoundException.IMAGE_NOT_FOUND;

@RestController
@RequestMapping("/api/v1/attachment")
public class AttachmentController {

    private final AttachmentService attachmentService;

    private final MessageSource messageSource;

    public AttachmentController(AttachmentService attachmentService,
                                MessageSource messageSource) {
        this.attachmentService = attachmentService;
        this.messageSource = messageSource;
    }

    @GetMapping("/{id}")
    public ResponseEntity<InputStreamResource> getById(@PathVariable Long id) {
        AttachmentDto attachDto = attachmentService.getById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, attachDto.getContentType());
        return new ResponseEntity<>(attachDto.getContent(), headers, HttpStatus.OK);
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException ex, Locale locale) {
        return messageSource.getMessage(IMAGE_NOT_FOUND, new Object[]{ ex.getId() }, locale);
    }

}
