package edu.bbte.projectbluebook.datacatalog.versioning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class, MongoAutoConfiguration.class})
public class VersioningApplication {
    public static void main(String[] args) {
        SpringApplication.run(VersioningApplication.class, args);
    }
}