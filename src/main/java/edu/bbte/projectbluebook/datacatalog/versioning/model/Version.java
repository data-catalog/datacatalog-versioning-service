package edu.bbte.projectbluebook.datacatalog.versioning.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Document
@CompoundIndexes({
        @CompoundIndex(name = "assetId_name", def = "{'assetId' : 1, 'name': 1}")
})
public class Version extends BaseEntity implements Serializable {
    private String name;

    private String assetId;

    private List<Content> contents = new ArrayList<>();
}
