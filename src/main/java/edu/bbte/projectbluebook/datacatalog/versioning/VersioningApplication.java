package edu.bbte.projectbluebook.datacatalog.versioning;

import edu.bbte.projectbluebook.datacatalog.versioning.config.ClientProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(exclude = { ReactiveSecurityAutoConfiguration.class})
@EnableConfigurationProperties(ClientProperties.class)
public class VersioningApplication {
    public static void main(String[] args) {
        SpringApplication.run(VersioningApplication.class, args);
    }
}