package edu.bbte.projectbluebook.datacatalog.versioning.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class LocationResponse {
    private String type;

    private List<ParameterResponse> parameters = null;
}
