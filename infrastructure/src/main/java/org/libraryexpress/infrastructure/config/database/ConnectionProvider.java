package org.libraryexpress.infrastructure.config.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.cdimascio.dotenv.Dotenv;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConnectionProvider implements AutoCloseable {

    private final HikariDataSource dataSource;

    public ConnectionProvider() {
        // Initializes Dotenv to read the .env file from the project root directory
        Dotenv dotenv = Dotenv.configure()
                .ignoreIfMissing() // Bypasses the file if running on AWS (or any cloud provider) production environment where variables are native
                .load();

        // Fetch credentials prioritizing native env variables, falling back to the .env file values
        String host = getEnvOrDotenv(dotenv, "DB_HOST", "localhost");
        String port = getEnvOrDotenv(dotenv, "DB_PORT", "5432");
        String database = getEnvOrDotenv(dotenv, "DB_NAME", "library_express");
        String username = getEnvOrDotenv(dotenv, "DB_USER", null);
        String password = getEnvOrDotenv(dotenv, "DB_PASSWORD", null);

        // Building the official non-pooled PostgreSQL connection string
        String jdbcUrl = String.format("jdbc:postgresql://%s:%s/%s", host, port, database);

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);

        // Load performance configurations from properties file
        Properties properties = this.loadProperties();

        // Binding variables using safe configuration loaders
        config.setMaximumPoolSize(
                Integer.parseInt(properties.getProperty("hikari.maximum-pool-size", "10"))
        );
        config.setMinimumIdle(
                Integer.parseInt(properties.getProperty("hikari.minimum-idle", "2"))
        );
        config.setIdleTimeout(
                Long.parseLong(properties.getProperty("hikari.idle-timeout", "600000"))
        );
        config.setConnectionTimeout(
                Long.parseLong(properties.getProperty("hikari.connection-timeout", "30000"))
        );


        // Initialization of the concurrent data source manager
        this.dataSource = new HikariDataSource(config);
    }

    /**
     * Exposes the configured DataSource abstraction to repositories or migration tools.
     */
    public DataSource getDataSource() {
        return this.dataSource;
    }

    /**
     * Helper logic to simulate native system environment fallback across multiple layers.
     */
    private String getEnvOrDotenv(Dotenv dotenv, String key, String defaultValue) {
        String systemEnv = System.getenv(key);
        if (systemEnv != null) return systemEnv;

        String dotenvValue = dotenv.get(key);
        return dotenvValue != null ? dotenvValue : defaultValue;
    }

    /**
     * Internal helper to load configuration streams directly from resources classpath.
     */
    private Properties loadProperties() {
        Properties props = new Properties();

        try (InputStream input = getClass().getClassLoader().getResourceAsStream("hikari.properties")) {
            if (input == null) {
                // Runtime fallback warning mechanism if the configuration deployment is corrupted
                System.err.println("Warning: hikari.properties not found. Relying on default parameters.");
                return props;
            }
            props.load(input);
        } catch (IOException e) {
            System.err.println("Error reading connection properties file: " + e.getMessage());
        }
        return props;
    }

    /**
     * Implements AutoCloseable to ensure thread-safe cleanup during shutdown hooks or test tearing steps.
     */
    @Override
    public void close() {
        if (this.dataSource != null && !this.dataSource.isClosed()) {
            this.dataSource.close();
        }
    }
}
