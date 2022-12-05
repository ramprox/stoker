package ru.stoker.testconfig;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.util.FileSystemUtils;
import ru.stoker.service.attachmentstorage.AttachmentStorageService;
import ru.stoker.service.attachmentstorage.LocalAttachmentStorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@TestConfiguration
public class ControllerITConfig {

    private Path tmpDir;

    @EventListener(classes = ContextClosedEvent.class)
    public void handleEventListener() throws IOException {
        FileSystemUtils.deleteRecursively(tmpDir);
    }

    @Bean
    public Path storagePath() throws IOException {
        Path path = Path.of("./picture_test_dir");
        if(Files.exists(path)) {
            FileSystemUtils.deleteRecursively(path);
        }
        tmpDir = Files.createDirectory(path);
        return tmpDir;
    }

    @Bean
    public AttachmentStorageService localAttachmentStorageService(Path storagePath) {
        return new LocalAttachmentStorageService(storagePath);
    }

}
