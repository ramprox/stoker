package ru.stoker.service.attachment;

import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import ru.stoker.model.AttachmentDto;

public interface AttachmentService {

    AttachmentDto getById(Long id);

    Long saveAttachment(Long userId, Long productId, String imageType, FileItemIterator fileItemIterator);

    void deleteById(Long id);

}
