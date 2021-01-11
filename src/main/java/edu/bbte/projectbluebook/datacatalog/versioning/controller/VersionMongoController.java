package edu.bbte.projectbluebook.datacatalog.versioning.controller;

import edu.bbte.projectbluebook.datacatalog.versioning.api.VersionApi;
import edu.bbte.projectbluebook.datacatalog.versioning.model.VersionRequest;
import edu.bbte.projectbluebook.datacatalog.versioning.model.VersionResponse;
import edu.bbte.projectbluebook.datacatalog.versioning.service.VersionMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.validation.Valid;
import java.util.List;

@RestController
public class VersionMongoController implements VersionApi {

    @Autowired
    private VersionMongoService versionMongoService;

    @Override
    public ResponseEntity<Void> createAssetVersion(String assetId, @Valid VersionRequest versionRequest) {
        /*RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String id = requestAttributes.getAttribute("userId", RequestAttributes.SCOPE_REQUEST).toString();
        String role = requestAttributes.getAttribute("role", RequestAttributes.SCOPE_REQUEST).toString();*/
        String id = "";
        String role = "admin";
        return versionMongoService.createAssetVersion(assetId, versionRequest, id, role);
    }

    @Override
    public ResponseEntity<Void> deleteAssetVersion(String assetId, String name) {
        /*RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String id = requestAttributes.getAttribute("userId", RequestAttributes.SCOPE_REQUEST).toString();
        String role = requestAttributes.getAttribute("role", RequestAttributes.SCOPE_REQUEST).toString();*/
        String id = "";
        String role = "admin";
        return versionMongoService.deleteAssetVersion(assetId, name, id, role);
    }

    @Override
    public ResponseEntity<VersionResponse> getAssetVersion(String assetId, String name) {
        /*RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String id = requestAttributes.getAttribute("userId", RequestAttributes.SCOPE_REQUEST).toString();
        String role = requestAttributes.getAttribute("role", RequestAttributes.SCOPE_REQUEST).toString();*/
        String id = "";
        String role = "admin";
        return versionMongoService.getAssetVersion(assetId, name, id, role);
    }

    @Override
    public ResponseEntity<List<VersionResponse>> getAssetVersions(String assetId) {
        /*RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        String id = requestAttributes.getAttribute("userId", RequestAttributes.SCOPE_REQUEST).toString();
        String role = requestAttributes.getAttribute("role", RequestAttributes.SCOPE_REQUEST).toString();*/
        String id = "";
        String role = "admin";
        return versionMongoService.getAssetVersions(assetId, id, role);
    }
}
