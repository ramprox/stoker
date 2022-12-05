package ru.stoker.service.product;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.stoker.database.entity.Attachment;
import ru.stoker.database.entity.Product;
import ru.stoker.database.entity.enums.ImageType;
import ru.stoker.database.repository.AttachmentRepository;
import ru.stoker.database.repository.CategoryRepository;
import ru.stoker.exceptions.Advt;
import ru.stoker.exceptions.Advt.UpdateOperationException;
import ru.stoker.exceptions.AttachmentEx;
import ru.stoker.exceptions.AttachmentStorage.FileOperationException;
import ru.stoker.exceptions.Category;
import ru.stoker.service.attachmentstorage.AttachmentStorageService;

import java.util.List;
import java.util.UUID;

@Service
public class ProductServiceImpl implements ProductService {

    private final CategoryRepository categoryRepository;

    private final AttachmentRepository attachmentRepository;

    private final AttachmentStorageService storageService;

    public ProductServiceImpl(CategoryRepository categoryRepository,
                              AttachmentRepository attachmentRepository,
                              AttachmentStorageService storageService) {
        this.categoryRepository = categoryRepository;
        this.attachmentRepository = attachmentRepository;
        this.storageService = storageService;
    }

    @Override
    public void save(Product product, List<MultipartFile> files) {
        try {
            checkCategoryExist(product.getCategory().getId());
            storageService.createBucket(product.getId());
            addAttachments(product, files);
        } catch (FileOperationException ex) {
            throw new Advt.SaveOperationException();
        }
    }

    @Override
    public void update(Product product, List<MultipartFile> files, List<Long> removingAttaches) {
        try {
            checkCategoryExist(product.getCategory().getId());
            removeAttachments(product, removingAttaches);
            addAttachments(product, files);
        } catch (FileOperationException ex) {
            throw new UpdateOperationException();
        }
    }

    @Override
    public void deleteAll(Long productId) {
        try {
            storageService.deleteBucket(productId);
        } catch (FileOperationException e) {
            throw new Advt.DeleteOperationException();
        }
    }

    private void checkCategoryExist(Long categoryId) {
        if(!categoryRepository.existsById(categoryId)) {
            throw new Category.NotFoundException(categoryId);
        }
    }

    private void addAttachments(Product product, List<MultipartFile> files) {
        if(files == null) {
            return;
        }
        files.stream().filter(file -> !file.isEmpty())
                .forEach(file -> {
                    String uri = UUID.randomUUID().toString();
                    Attachment attachment = new Attachment(uri);
                    attachment.setContentType(ImageType.fromString(file.getContentType()));
                    attachmentRepository.save(attachment);
                    product.addAttachment(attachment);
                    storageService.saveFile(product.getId(), uri, file);
        });
    }

    private void removeAttachments(Product product, List<Long> removedAttachments) {
        if(removedAttachments == null) {
            return;
        }
        removedAttachments.forEach(id -> {
            Attachment attachment = product.removeById(id);
            if(attachment == null) {
                throw new AttachmentEx.NotFoundException(id);
            }
            storageService.deleteFile(product.getId(), attachment.getFilename());
        });
    }

}
