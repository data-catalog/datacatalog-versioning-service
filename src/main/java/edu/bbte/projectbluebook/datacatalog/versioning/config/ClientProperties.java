package edu.bbte.projectbluebook.datacatalog.versioning.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;

@Data
@Validated
@ConfigurationProperties(prefix = "client")
public class ClientProperties {
    @NotEmpty
    private String assetServiceUri;

    @NotEmpty
    private String userServiceUri;

    @NotEmpty
    private String token;
}
