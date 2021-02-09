package edu.bbte.projectbluebook.datacatalog.versioning.model.dto;

import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class AssetResponse {
    private String id;

    private OffsetDateTime createdAt;

    private OffsetDateTime updatedAt;

    private String name;

    private String description;

    private LocationResponse location;

    private List<String> tags = null;

    private String ownerId;
}
