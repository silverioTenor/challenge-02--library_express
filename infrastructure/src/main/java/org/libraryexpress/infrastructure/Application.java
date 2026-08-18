package org.libraryexpress.infrastructure;

import org.libraryexpress.infrastructure.cli.ManagementCli;
import org.libraryexpress.infrastructure.config.AppContext;
import org.libraryexpress.infrastructure.config.database.ConnectionProvider;
import org.libraryexpress.infrastructure.config.database.MigrationRunner;

import javax.sql.DataSource;

/**
 * Main application bootstrapper.
 * Responsible for orchestration, dependency injection initialization, and resource lifecycle management.
 */
public class Application {
    public static void main(String[] args) {

        System.out.println("Starting LibraryExpress infrastructure pipeline...");

        ConnectionProvider connectionProvider = prepareDatabaseConnection();

        AppContext context = new AppContext(connectionProvider);

        System.out.println("Application booted successfully! Ready for executions.");

        initCLI(context, connectionProvider);
    }

    private static void initCLI(AppContext context, ConnectionProvider connectionProvider) {
        var mgmt = new ManagementCli(context, connectionProvider);
        mgmt.app();
    }

    private static ConnectionProvider prepareDatabaseConnection() {
        // 1. Initialize the managed connection pool resource
        ConnectionProvider connectionProvider = new ConnectionProvider();

        try {
            DataSource dataSource = connectionProvider.getDataSource();

            // 2. Execute Flyway database schema migrations
            System.out.println("Executing schema migrations...");
            MigrationRunner.run(dataSource);
            System.out.println("Database schema is fully synchronized!");

            // 3. Register a graceful shutdown hook to release database resources on JVM exit
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                System.out.println("Shutting down connection pool gracefully...");
                connectionProvider.close();
                System.out.println("Database resources released safely.");
            }));

        } catch (Exception e) {
            System.err.println("FATAL: Application startup failed dynamically: " + e.getMessage());
            connectionProvider.close();
            System.exit(1);
        }

        return connectionProvider;
    }
}