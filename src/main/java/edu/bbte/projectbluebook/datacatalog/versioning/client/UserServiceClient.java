package edu.bbte.projectbluebook.datacatalog.versioning.client;

import edu.bbte.projectbluebook.datacatalog.versioning.config.ClientProperties;
import edu.bbte.projectbluebook.datacatalog.versioning.model.dto.TokenInfoRequest;
import edu.bbte.projectbluebook.datacatalog.versioning.model.dto.TokenInfoResponse;
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
        TokenInfoRequest request = new TokenInfoRequest().token(token);

        return webClient
                .post().uri("/token_info")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(TokenInfoResponse.class);
    }
}
