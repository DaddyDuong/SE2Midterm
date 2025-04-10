package fit.se2.SE2Midterm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Initializing database with SQL scripts...");
        executeScriptFromFile("sql/config.sql");
        System.out.println("Database initialization completed.");
    }

    private void executeScriptFromFile(String path) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            String sql = new BufferedReader(new InputStreamReader(resource.getInputStream()))
                    .lines()
                    .collect(Collectors.joining("\n"));
            
            // Split by semicolon and execute each statement separately
            for (String statement : sql.split(";")) {
                if (!statement.trim().isEmpty()) {
                    try {
                        jdbcTemplate.execute(statement);
                    } catch (Exception e) {
                        System.out.println("Error executing SQL statement: " + e.getMessage());
                        // Continue with next statement
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading SQL file: " + e.getMessage());
        }
    }
} 