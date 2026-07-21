package org.libraryexpress.infrastructure.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.libraryexpress.infrastructure.config.JacksonConfig;

public final class JsonPrinter {

    private JsonPrinter() {}

    public static String print(Object obj) {
        try {
            return JacksonConfig.prettyWriter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            return String.valueOf(obj);
        }
    }
}
