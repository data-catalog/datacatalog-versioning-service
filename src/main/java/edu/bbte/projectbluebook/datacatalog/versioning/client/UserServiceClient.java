package edu.bbte.projectbluebook.datacatalog.versioning.client;

import edu.bbte.projectbluebook.datacatalog.versioning.config.ClientProperties;
import edu.bbte.projectbluebook.datacatalog.versioning.model.dto.TokenInfoResponse;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class UserServiceClient {
    private final WebClient webClient;

    public UserServiceClient(ClientProperties clientProperties, WebClient.Builder webClientBuilder) {
        final String uri = clientProperties.getUserServiceUri();

        this.webClient = webClientBuilder.baseUrl(uri).build();
    }

    public Mono<TokenInfoResponse> getTokenInfo(String token) {
        return webClient
                .post().uri("/token_info")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(token)
                .retrieve()
                .bodyToMono(TokenInfoResponse.class);
    }

}
