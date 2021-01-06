package edu.bbte.projectbluebook.datacatalog.versioning.service;

import com.mongodb.client.FindIterable;
import edu.bbte.projectbluebook.datacatalog.versioning.helpers.Utility;
import edu.bbte.projectbluebook.datacatalog.versioning.model.ContentResponse;
import edu.bbte.projectbluebook.datacatalog.versioning.model.VersionRequest;
import edu.bbte.projectbluebook.datacatalog.versioning.model.VersionResponse;
import edu.bbte.projectbluebook.datacatalog.versioning.repository.VersionMongoRepository;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
public class VersionMongoService {

    @Autowired
    private VersionMongoRepository versionMongoRepository;

    public ResponseEntity<Void> createAssetVersion(String assetId, @Valid VersionRequest versionRequest, String uid, String role) {
        Document id;
        try {
            id = new Document("_id", new ObjectId(assetId));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        int ownership = versionMongoRepository.isOwnerOfAsset(id, uid);
        switch (ownership) {
            case -1:
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            case 0:
                if (!role.equals("admin")) {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
        }
        if (versionMongoRepository.versionExists(assetId, versionRequest.getName())) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Version with given name on asset already exists!");
        }
        Document version = new Document();
        version.append("assetId", assetId);
        version.append("name", versionRequest.getName());
        Document assetData = versionMongoRepository.getAssetData(id);
        List<Document> content = Utility.getVersionContent(assetData);
        version.append("content", content);

        return versionMongoRepository.insert(version)
                ? new ResponseEntity<>(HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public ResponseEntity<Void> deleteAssetVersion(String assetId, String name, String uid, String role) {
        Document id;
        try {
            id = new Document("_id", new ObjectId(assetId));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        int ownership = versionMongoRepository.isOwnerOfAsset(id, uid);
        switch (ownership) {
            case -1:
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            case 0:
                if (!role.equals("admin")) {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
        }
        Document deleted = versionMongoRepository.delete(assetId, name);
        if (deleted == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    public ResponseEntity<VersionResponse> getAssetVersion(String assetId, String name, String uid, String role) {
        Document id;
        try {
            id = new Document("_id", new ObjectId(assetId));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        int ownership = versionMongoRepository.isOwnerOfAsset(id, uid);
        switch (ownership) {
            case -1:
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            case 0:
                if (!role.equals("admin")) {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
        }
        Document version = versionMongoRepository.findAssetVersion(assetId, name);
        if (version == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        VersionResponse versionResponse = new VersionResponse();
        versionResponse.setAssetId(assetId);
        versionResponse.setName(name);
        versionResponse.setCreatedAt(version
                .getDate("createdAt")
                .toInstant()
                .atOffset(ZoneOffset.UTC)
        );
        List<Document> cont = version.getList("content", Document.class);
        List<ContentResponse> content = new ArrayList<>();
        cont.forEach(item -> content.add(Utility.convertDocToContentResp(item)));
        versionResponse.setContents(content);
        return new ResponseEntity<>(versionResponse, HttpStatus.OK);
    }

    public ResponseEntity<List<VersionResponse>> getAssetVersions(String assetId, String uid, String role) {
        Document id;
        try {
            id = new Document("_id", new ObjectId(assetId));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        int ownership = versionMongoRepository.isOwnerOfAsset(id, uid);
        switch (ownership) {
            case -1:
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            case 0:
                if (!role.equals("admin")) {
                    return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
                }
        }
        FindIterable<Document> versions = versionMongoRepository.findVersions(assetId);
        List<VersionResponse> responses = new ArrayList<>();
        for(Document version : versions) {
            VersionResponse versionResponse = new VersionResponse();
            versionResponse.setAssetId(assetId);
            versionResponse.setName(version.getString("name"));
            versionResponse.setCreatedAt(version
                    .getDate("createdAt")
                    .toInstant()
                    .atOffset(ZoneOffset.UTC)
            );
            List<Document> cont = version.getList("content", Document.class);
            List<ContentResponse> content = new ArrayList<>();
            cont.forEach(item -> content.add(Utility.convertDocToContentResp(item)));
            versionResponse.setContents(content);
            responses.add(versionResponse);
        }
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }
}
