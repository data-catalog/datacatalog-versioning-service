package edu.bbte.projectbluebook.datacatalog.versioning.client;

import edu.bbte.projectbluebook.datacatalog.versioning.config.ClientProperties;
import edu.bbte.projectbluebook.datacatalog.versioning.model.dto.AssetResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class AssetServiceClient {
    private final WebClient webClient;

    public AssetServiceClient(ClientProperties clientProperties, WebClient.Builder webClientBuilder) {
        final String uri = clientProperties.getAssetServiceUri();

        this.webClient = webClientBuilder.baseUrl(uri).build();
    }

    public Mono<AssetResponse> fetchAsset(String assetId) {
        return webClient
                .get().uri("/assets/{id}", assetId)
                .retrieve()
                .onStatus(status -> status.equals(HttpStatus.NOT_FOUND), err -> Mono.empty())
                .bodyToMono(AssetResponse.class);
    }
}
