package edu.bbte.projectbluebook.datacatalog.versioning.model.mapper;

import edu.bbte.projectbluebook.datacatalog.versioning.model.Version;
import edu.bbte.projectbluebook.datacatalog.versioning.model.dto.VersionRequest;
import edu.bbte.projectbluebook.datacatalog.versioning.model.dto.VersionResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class VersionMapper {
    @Mapping(target = "assetId", source = "assetId")
    public abstract Version requestDtoToModel(VersionRequest versionRequest, String assetId);

    public abstract VersionResponse modelToResponseDto(Version version);
}
