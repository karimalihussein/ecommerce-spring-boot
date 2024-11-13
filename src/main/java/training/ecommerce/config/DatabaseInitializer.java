package training.ecommerce.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;

@Configuration
public class DatabaseInitializer {

    @Bean
    public CommandLineRunner loadData(DataSource dataSource) {
        return args -> {
            try (Connection connection = dataSource.getConnection();
                 Statement statement = connection.createStatement()) {

                // // Load the SQL file and execute it
                // String sql = Files.readString(Paths.get("src/main/resources/sql/data.sql"));
                // statement.execute(sql);
                // System.out.println("Database initialized successfully!");
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Failed to initialize the database: " + e.getMessage());
            }
        };
    }
}