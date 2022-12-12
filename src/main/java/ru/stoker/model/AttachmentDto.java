package ru.stoker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;
import java.io.OutputStream;

@Getter
@AllArgsConstructor
public class AttachmentDto {

    private InputStreamResource content;

    private String contentType;

}
