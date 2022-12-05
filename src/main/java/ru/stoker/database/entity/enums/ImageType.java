package ru.stoker.database.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.MediaType;
import ru.stoker.exceptions.AttachmentStorage;

import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public enum ImageType {

    JPEG(MediaType.IMAGE_JPEG_VALUE),
    PNG(MediaType.IMAGE_PNG_VALUE),
    GIF(MediaType.IMAGE_GIF_VALUE);

    final String type;

    public static ImageType fromString(String type) {
        if(type == null) {
            return null;
        }
        return Stream.of(ImageType.values())
                .filter(imageType -> imageType.getType().equals(type))
                .findFirst()
                .orElseThrow(() -> buildUnknownImageTypeException(type));
    }

    private static AttachmentStorage.UnknownImageTypeException buildUnknownImageTypeException(String type) {
        String message = String.format("Неизвестный тип изображения: '%s'", type);
        return new AttachmentStorage.UnknownImageTypeException(message);
    }

}
