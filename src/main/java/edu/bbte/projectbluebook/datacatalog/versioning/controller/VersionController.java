package edu.bbte.projectbluebook.datacatalog.versioning.controller;

import edu.bbte.projectbluebook.datacatalog.versioning.api.VersionApi;
import edu.bbte.projectbluebook.datacatalog.versioning.model.dto.VersionRequest;
import edu.bbte.projectbluebook.datacatalog.versioning.model.dto.VersionResponse;
import edu.bbte.projectbluebook.datacatalog.versioning.service.VersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
public class VersionController implements VersionApi {

    @Autowired
    private VersionService service;

    @Override
    public Mono<ResponseEntity<Void>> createAssetVersion(String assetId, @Valid Mono<VersionRequest> versionRequest,
                                                         ServerWebExchange exchange) {
        return service
                .createAssetVersion(assetId, versionRequest)
                .map(nothing -> ResponseEntity.status(HttpStatus.CREATED).build());
    }

    @Override
    public Mono<ResponseEntity<Void>> deleteAssetVersion(String assetId, String name, ServerWebExchange exchange) {
        return service
                .deleteAssetVersion(assetId, name)
                .map(nothing -> ResponseEntity.noContent().build());
    }

    @Override
    public Mono<ResponseEntity<VersionResponse>> getAssetVersion(String assetId, String name,
                                                                 ServerWebExchange exchange) {
        return service
                .getAssetVersion(assetId, name)
                .map(ResponseEntity::ok);
    }

    @Override
    public Mono<ResponseEntity<Flux<VersionResponse>>> getAssetVersions(String assetId, ServerWebExchange exchange) {
        Flux<VersionResponse> versions = service
                .getAssetVersions(assetId);

        return Mono
                .just(versions)
                .map(ResponseEntity::ok);
    }
}
