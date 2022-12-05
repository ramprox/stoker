package ru.stoker.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttachmentDto {

    private byte[] content;

    private String contentType;

}
