package edu.bbte.projectbluebook.datacatalog.versioning.repository;

import edu.bbte.projectbluebook.datacatalog.versioning.model.Version;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface VersionRepository extends ReactiveMongoRepository<Version, String> {
    Mono<Void> deleteByAssetIdAndName(String assetId, String name);

    Mono<Version> findFirstByAssetIdAndName(String assetId, String name);

    Flux<Version> findAllByAssetId(String assetId);
}
