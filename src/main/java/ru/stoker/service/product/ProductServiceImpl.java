package ru.stoker.service.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.stoker.exceptions.Advt;
import ru.stoker.exceptions.AttachmentStorage.FileOperationException;
import ru.stoker.service.attachmentstorage.AttachmentStorageService;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final AttachmentStorageService storageService;

    @Override
    public void deleteAll(Long productId) {
        try {
            storageService.deleteBucket(productId);
        } catch (FileOperationException e) {
            throw new Advt.DeleteOperationException();
        }
    }

}
