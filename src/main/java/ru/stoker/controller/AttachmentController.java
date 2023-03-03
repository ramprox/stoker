package ru.stoker.controller;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.context.MessageSource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.stoker.exceptions.AttachmentEx.NotFoundException;
import ru.stoker.model.AttachmentDto;
import ru.stoker.service.attachment.AttachmentService;
import ru.stoker.service.security.StokerUserDetails;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Locale;

import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static ru.stoker.exceptions.AttachmentEx.NotFoundException.IMAGE_NOT_FOUND;

@RestController
@RequestMapping("/api/v1/attachment")
@RequiredArgsConstructor
public class AttachmentController {

    private final AttachmentService attachmentService;

    private final MessageSource messageSource;

    @GetMapping("/{id}")
    public ResponseEntity<InputStreamResource> getById(@PathVariable Long id) {
        AttachmentDto attachDto = attachmentService.getById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.add(CONTENT_TYPE, attachDto.getContentType());
        return new ResponseEntity<>(attachDto.getContent(), headers, OK);
    }

    @PostMapping("/{productId}")
    @PreAuthorize("hasAuthority('USER')")
    public Long upload(@PathVariable Long productId,
                         @AuthenticationPrincipal StokerUserDetails userDetails,
                         HttpServletRequest request) throws IOException {
        if(!ServletFileUpload.isMultipartContent(request)) {
            throw new RuntimeException("Multipart request expected");
        }
        String imageType = request.getHeader("Image-type");
        FileItemIterator fileItemIterator = new ServletFileUpload().getItemIterator(request);
        return attachmentService.saveAttachment(userDetails.getId(), productId, imageType, fileItemIterator);
    }

    @DeleteMapping("/{id}")
    public String deleteById(@PathVariable Long id) {
        attachmentService.deleteById(id);
        return "Success";
    }

    @ResponseStatus(NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String handleNotFoundException(NotFoundException ex, Locale locale) {
        return messageSource.getMessage(IMAGE_NOT_FOUND, new Object[]{ ex.getId() }, locale);
    }

}
