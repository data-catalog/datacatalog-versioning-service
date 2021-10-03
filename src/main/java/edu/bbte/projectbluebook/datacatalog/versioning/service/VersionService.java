package edu.bbte.projectbluebook.datacatalog.versioning.service;

import com.azure.storage.blob.BlobContainerClientBuilder;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.common.StorageSharedKeyCredential;
import com.mongodb.DuplicateKeyException;
import edu.bbte.projectbluebook.datacatalog.versioning.client.AssetServiceClient;
import edu.bbte.projectbluebook.datacatalog.versioning.exception.NotFoundException;
import edu.bbte.projectbluebook.datacatalog.versioning.exception.VersionServiceException;
import edu.bbte.projectbluebook.datacatalog.versioning.model.Content;
import edu.bbte.projectbluebook.datacatalog.versioning.model.Version;
import edu.bbte.projectbluebook.datacatalog.versioning.model.dto.AssetResponse;
import edu.bbte.projectbluebook.datacatalog.versioning.model.dto.ParameterResponse;
import edu.bbte.projectbluebook.datacatalog.versioning.model.dto.VersionRequest;
import edu.bbte.projectbluebook.datacatalog.versioning.model.dto.VersionResponse;
import edu.bbte.projectbluebook.datacatalog.versioning.model.mapper.VersionMapper;
import edu.bbte.projectbluebook.datacatalog.versioning.repository.VersionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class VersionService {

    @Autowired
    private VersionRepository repository;

    @Autowired
    private VersionMapper mapper;

    @Autowired
    private AssetServiceClient assetServiceClient;

    public Mono<Void> createAssetVersion(String assetId, Mono<VersionRequest> versionRequest) {
        return versionRequest
                .map(request -> mapper.requestDtoToModel(request, assetId))
                .flatMap(version -> fillVersionContent(version, assetId))
                .flatMap(version -> repository.insert(version))
                .onErrorMap(DuplicateKeyException.class, (e) ->
                        new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                        "This version name is already used for the asset."))
                .then();
    }

    private Mono<Version> fillVersionContent(Version version, String assetId) {
        return assetServiceClient.fetchAsset(assetId)
                .switchIfEmpty(Mono.error(new NotFoundException("Asset not found.")))
                .flatMap(this::fetchVersionContent)
                .map(contents -> {
                    version.setContents(contents);
                    return version;
                });
    }

    private Mono<List<Content>> fetchVersionContent(AssetResponse assetResponse) {
        // TODO: handle exceptions
        Map<String, String> parameters = assetResponse
                .getLocation()
                .getParameters().stream()
                .collect(Collectors.toMap(ParameterResponse::getKey, ParameterResponse::getValue));

        String accountName;
        try {
            accountName = parameters.get("accountUrl").split("\\.")[0].replaceFirst("https?://", "");
        } catch (IndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The accuont URL is malformed.");
        }
        
        String sasToken = parameters.get("sasToken");
        String accountKey = parameters.get("accountKey");

        try {
            BlobContainerClientBuilder builder = new BlobContainerClientBuilder()
                    .endpoint(parameters.get("accountUrl"))
                    .containerName(parameters.get("containerName"));

            if (sasToken != null) {
                builder.sasToken(sasToken);
            }
            if (accountKey != null) {
                builder.credential(new StorageSharedKeyCredential(accountName, accountKey));
            }

            return builder.buildAsyncClient()
                    .listBlobs()
                    .map(this::blobToContent)
                    .collectList();
        } catch (IllegalArgumentException e) {
            throw new VersionServiceException(e.getMessage());
        }

    }

    private Content blobToContent(BlobItem blob) {
        Content content = new Content();

        content.setName(blob.getName());
        content.setLastModified(blob.getProperties().getLastModified());
        content.setSize(blob.getProperties().getContentLength());

        return content;
    }

    public Mono<Void> deleteAssetVersion(String assetId, String name) {
        return repository.deleteByAssetIdAndName(assetId, name);
    }

    // TODO: check if asset exists first
    public Mono<VersionResponse> getAssetVersion(String assetId, String name) {
        return repository.findFirstByAssetIdAndName(assetId, name)
                .map(mapper::modelToResponseDto)
                .switchIfEmpty(Mono.error(new NotFoundException("Version not found.")));
    }

    // TODO: check if asset exists first
    public Flux<VersionResponse> getAssetVersions(String assetId) {
        return repository.findAllByAssetId(assetId)
                .map(mapper::modelToResponseDto);
    }
}
