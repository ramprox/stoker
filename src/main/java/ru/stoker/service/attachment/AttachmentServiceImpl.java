package ru.stoker.service.attachment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import ru.stoker.database.entity.Attachment;
import ru.stoker.database.repository.AttachmentRepository;
import ru.stoker.exceptions.AttachmentEx;
import ru.stoker.exceptions.AttachmentStorage;
import ru.stoker.model.AttachmentDto;
import ru.stoker.service.attachmentstorage.AttachmentStorageService;

import java.io.InputStream;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;

    private final AttachmentStorageService attachmentStorageService;

    @Autowired
    public AttachmentServiceImpl(AttachmentRepository attachmentRepository,
                                 AttachmentStorageService attachmentStorageService) {
        this.attachmentRepository = attachmentRepository;
        this.attachmentStorageService = attachmentStorageService;
    }

    @Override
    public AttachmentDto getById(Long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new AttachmentEx.NotFoundException(id));
        Long productId = attachment.getProduct().getId();
        String filename = attachment.getFilename();
        try {
            InputStream stream = attachmentStorageService.getByProductIdAndFilename(productId, filename);
            InputStreamResource content = new InputStreamResource(stream);
            return new AttachmentDto(content, attachment.getContentType().getType());
        } catch (AttachmentStorage.FileOperationException e) {
            throw new AttachmentEx.NotFoundException(id);
        }
    }

}
