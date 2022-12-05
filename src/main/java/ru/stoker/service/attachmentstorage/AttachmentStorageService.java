package ru.stoker.service.attachmentstorage;

import org.springframework.web.multipart.MultipartFile;

public interface AttachmentStorageService {

    void createBucket(Long productId);

    void deleteBucket(Long productId);

    void saveFile(Long productId, String filename, MultipartFile file);

    void deleteFile(Long productId, String filename);

    byte[] getByProductIdAndUri(Long productId, String uri);

}
