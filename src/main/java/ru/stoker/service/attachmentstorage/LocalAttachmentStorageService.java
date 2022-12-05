package ru.stoker.service.attachmentstorage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.stoker.exceptions.AttachmentStorage.FileOperationException;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Slf4j
@Service
public class LocalAttachmentStorageService implements AttachmentStorageService {

    private final Path storagePath;

    public LocalAttachmentStorageService(@Value("${picture.storage.path}") Path storagePath) {
        this.storagePath = storagePath;
    }

    @PostConstruct
    public void init() {
        try {
            if(!Files.exists(storagePath)) {
                Files.createDirectory(storagePath);
                log.info("Создана директория для картинок: {}", storagePath);
            }
        } catch (IOException e) {
            String message = String.format("Не могу создать директорию '%s'", storagePath);
            log.error(message);
            throw new FileOperationException(message, storagePath);
        }
    }

    @Override
    public void createBucket(Long productId) {
        Path path = getPathByProductId(productId);
        try {
            if(!Files.exists(path)) {
                Files.createDirectory(path);
                log.info("Создана директория для продукта с id = {}. Путь: {}", productId, path);
            }
        } catch (IOException ex) {
            String message = String.format("Не могу создать директорию '%s'", path);
            log.error(message);
            throw new FileOperationException(message, path);
        }
    }

    @Override
    public byte[] getByProductIdAndUri(Long productId, String uri) {
        Path path = getPathByProductId(productId).resolve(uri);
        try {
            return Files.readAllBytes(path);
        } catch (IOException ex) {
            String message = String.format("Не могу прочитать содержимое файла '%s'", path);
            log.error(message);
            throw new FileOperationException(message, path);
        }
    }

    @Override
    public void saveFile(Long productId, String filename, MultipartFile file) {
        Path path = getPathByProductId(productId);
        Path localFilePath = path.resolve(filename);
        writeFile(localFilePath, file);
    }

    @Override
    public void deleteFile(Long productId, String filename) {
        Path productPath = getPathByProductId(productId);
        Path filePath = productPath.resolve(filename);
        try {
            Files.delete(filePath);
            log.info("Файл удален: {}", filePath);
        } catch (IOException e) {
            String message = String.format("Не могу удалить файл '%s'", filePath);
            log.error(message);
            throw new FileOperationException(message, filePath);
        }
    }

    @Override
    public void deleteBucket(Long productId) {
        Path path = getPathByProductId(productId);
        try {
            FileSystemUtils.deleteRecursively(path);
            log.info("Директория с картинками для продукта с id = {} удалена. Путь: {}", productId, path);
        } catch (IOException e) {
            String message = String.format("Не могу удалить директорию '%s'", path);
            log.error(message);
            throw new FileOperationException(message, path);
        }
    }

    private Path getPathByProductId(Long productId) {
        return storagePath.resolve(productId.toString());
    }

    private void writeFile(Path path, MultipartFile file) {
        try {
            Files.write(path, file.getBytes());
            log.info("Записано содержимое картинки по пути: {}", path);
        } catch (IOException ex) {
            String message = String.format("Не могу записать в файл '%s'", path);
            log.error(message);
            throw new FileOperationException(message, path);
        }
    }

}
