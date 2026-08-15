package org.libraryexpress.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class JacksonConfig {

    private static final ObjectMapper MAPPER = buildMapper();

    private JacksonConfig() {}

    private static ObjectMapper buildMapper() {
        return new ObjectMapper()
                .registerModule(new JavaTimeModule()) // To support LocalDate, LocalDateTime etc.
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static ObjectMapper mapper() {
        return MAPPER;
    }

    public static ObjectWriter prettyWriter() {
        return MAPPER.writerWithDefaultPrettyPrinter();
    }
}
