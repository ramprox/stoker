package ru.stoker.util.factory;

import org.springframework.core.io.ByteArrayResource;

public class ByteArrayResourceFactory {

    public static ByteArrayResource createByteArrayResource(byte[] content) {
        return new ByteArrayResource(content) {
            @Override
            public String getFilename() {
                return "files";
            }
        };
    }

}
