package edu.bbte.projectbluebook.datacatalog.versioning.model.dto;

import lombok.Data;

@Data
public class TokenInfoResponse {
    private Boolean active;

    private String userId;

    private String username;

    private String role;

    private Integer exp;

    private Integer iat;
}
