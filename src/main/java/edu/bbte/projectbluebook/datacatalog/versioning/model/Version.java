package edu.bbte.projectbluebook.datacatalog.versioning.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Document
public class Version extends BaseEntity implements Serializable {
    @Indexed(unique = true)
    private String name;

    private String assetId;

    private List<Content> contents = new ArrayList<>();
}
