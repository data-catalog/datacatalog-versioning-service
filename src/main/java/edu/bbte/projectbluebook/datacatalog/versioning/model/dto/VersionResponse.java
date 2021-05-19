package edu.bbte.projectbluebook.datacatalog.versioning.model.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import edu.bbte.projectbluebook.datacatalog.versioning.model.dto.ContentResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 
 */
@ApiModel(description = "")

public class VersionResponse  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("name")
  private String name;

  @JsonProperty("assetId")
  private String assetId;

  @JsonProperty("contents")
  @Valid
  private List<ContentResponse> contents = new ArrayList<>();

  @JsonProperty("createdAt")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime createdAt;

  public VersionResponse name(String name) {
    this.name = name;
    return this;
  }

  /**
   * The name of the version. Unique for an asset.
   * @return name
  */
  @ApiModelProperty(required = true, value = "The name of the version. Unique for an asset.")
  @NotNull


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public VersionResponse assetId(String assetId) {
    this.assetId = assetId;
    return this;
  }

  /**
   * The ID of the asset having this version.
   * @return assetId
  */
  @ApiModelProperty(required = true, value = "The ID of the asset having this version.")
  @NotNull


  public String getAssetId() {
    return assetId;
  }

  public void setAssetId(String assetId) {
    this.assetId = assetId;
  }

  public VersionResponse contents(List<ContentResponse> contents) {
    this.contents = contents;
    return this;
  }

  public VersionResponse addContentsItem(ContentResponse contentsItem) {
    this.contents.add(contentsItem);
    return this;
  }

  /**
   * The list of blobs which are present in this version.
   * @return contents
  */
  @ApiModelProperty(required = true, value = "The list of blobs which are present in this version.")
  @NotNull

  @Valid

  public List<ContentResponse> getContents() {
    return contents;
  }

  public void setContents(List<ContentResponse> contents) {
    this.contents = contents;
  }

  public VersionResponse createdAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  /**
   * The datetime when this version was created.
   * @return createdAt
  */
  @ApiModelProperty(required = true, value = "The datetime when this version was created.")
  @NotNull

  @Valid

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VersionResponse versionResponse = (VersionResponse) o;
    return Objects.equals(this.name, versionResponse.name) &&
        Objects.equals(this.assetId, versionResponse.assetId) &&
        Objects.equals(this.contents, versionResponse.contents) &&
        Objects.equals(this.createdAt, versionResponse.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, assetId, contents, createdAt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VersionResponse {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    assetId: ").append(toIndentedString(assetId)).append("\n");
    sb.append("    contents: ").append(toIndentedString(contents)).append("\n");
    sb.append("    createdAt: ").append(toIndentedString(createdAt)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

