package ru.stoker.service.attachmentstorage;

import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

public interface AttachmentStorageService {

    void createBucket(Long productId);

    void deleteBucket(Long productId);

    void saveFile(Long productId, String filename, MultipartFile file);

    void deleteFile(Long productId, String filename);

    InputStream getByProductIdAndFilename(Long productId, String filename);

}
