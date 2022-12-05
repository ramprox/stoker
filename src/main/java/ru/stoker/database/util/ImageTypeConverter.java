package ru.stoker.database.util;

import ru.stoker.database.entity.enums.ImageType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class ImageTypeConverter implements AttributeConverter<ImageType, String> {
    @Override
    public String convertToDatabaseColumn(ImageType imageType) {
        return imageType != null ? imageType.getType() : null;
    }

    @Override
    public ImageType convertToEntityAttribute(String type) {
        return ImageType.fromString(type);
    }

}
