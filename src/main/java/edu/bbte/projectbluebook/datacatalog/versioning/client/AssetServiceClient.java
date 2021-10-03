package edu.bbte.projectbluebook.datacatalog.versioning.client;

import edu.bbte.projectbluebook.datacatalog.versioning.config.ClientProperties;
import edu.bbte.projectbluebook.datacatalog.versioning.model.dto.AssetResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
public class AssetServiceClient {
    private final WebClient webClient;

    public AssetServiceClient(ClientProperties clientProperties, WebClient.Builder webClientBuilder) {
        final String uri = clientProperties.getAssetServiceUri();
        final String token = clientProperties.getToken();

        this.webClient = webClientBuilder.baseUrl(uri).defaultHeader("Authorization", "Bearer " + token).build();
    }

    public Mono<AssetResponse> fetchAsset(String assetId) {
        return webClient
                .get().uri("/assets/{id}", assetId)
                .retrieve()
                .bodyToMono(AssetResponse.class)
                .onErrorResume(WebClientResponseException.class,
                        e -> e.getRawStatusCode() == 404 ? Mono.empty() : Mono.error(e));
    }
}
