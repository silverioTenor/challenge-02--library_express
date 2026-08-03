package org.libraryexpress.infrastructure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.libraryexpress.infrastructure.config.JacksonConfig;

public final class JsonReader {

    private JsonReader() {}

    public static <T> T read(String json, Class<T> targetType) {
        try {
            return JacksonConfig.mapper().readValue(json, targetType);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Invalid JSON for type " + targetType.getSimpleName(), e);
        }
    }
}
