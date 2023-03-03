package ru.stoker.service.attachmentstorage;

import org.apache.tomcat.util.http.fileupload.FileItemIterator;

import java.io.InputStream;

public interface AttachmentStorageService {

    void createBucket(Long productId);

    void deleteBucket(Long productId);

    void saveFile(Long productId, String filename, FileItemIterator fileItemIterator);

    void deleteFile(Long productId, String filename);

    InputStream getByProductIdAndFilename(Long productId, String filename);

}
