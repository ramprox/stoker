package ru.stoker.mapper;

import ru.stoker.database.entity.Attachment;

import java.util.List;

import static java.util.stream.Collectors.toList;

public interface AttachmentToLong {

    default List<Long> toListAttIds(List<Attachment> attachments) {
        return attachments.stream().map(Attachment::getId).collect(toList());
    }

}
