package edu.bbte.projectbluebook.datacatalog.versioning.model.dto;

import lombok.Data;

@Data
public class TokenInfoRequest {
    private String token;

    private String token_type_hint;
}
