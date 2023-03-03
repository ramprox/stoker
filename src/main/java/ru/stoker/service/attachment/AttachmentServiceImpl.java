package ru.stoker.service.attachment;

import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stoker.database.entity.Attachment;
import ru.stoker.database.entity.Product;
import ru.stoker.database.entity.enums.ImageType;
import ru.stoker.database.repository.AttachmentRepository;
import ru.stoker.database.repository.ProductRepository;
import ru.stoker.exceptions.AttachmentEx;
import ru.stoker.exceptions.AttachmentStorage;
import ru.stoker.exceptions.ProductEx;
import ru.stoker.model.AttachmentDto;
import ru.stoker.service.attachmentstorage.AttachmentStorageService;

import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {

    private final AttachmentRepository attachmentRepository;

    private final AttachmentStorageService attachmentStorageService;

    private final ProductRepository productRepository;

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

    @Override
    public Long saveAttachment(Long userId, Long productId, String imageType, FileItemIterator fileItemIterator) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductEx.NotFoundException(productId));
        if(!userId.equals(product.getAdvertisement().getUser().getId())) {
            throw new RuntimeException("Доступ запрещен");
        }
        String filename = UUID.randomUUID().toString();
        attachmentStorageService.createBucket(productId);
        attachmentStorageService.saveFile(productId, filename, fileItemIterator);
        Attachment attachment = new Attachment(filename);
        attachment.setContentType(ImageType.fromString(imageType));
        attachment.setProduct(product);
        return attachmentRepository.save(attachment).getId();
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Attachment attachment = attachmentRepository.findById(id)
                .orElseThrow(() -> new AttachmentEx.NotFoundException(id));
        Long productId = attachment.getProduct().getId();
        attachmentStorageService.deleteFile(productId, attachment.getFilename());
        attachmentRepository.deleteById(id);
        if(!attachmentRepository.existsByProductId(attachment.getProduct().getId())) {
            attachmentStorageService.deleteBucket(productId);
        }
    }

}
