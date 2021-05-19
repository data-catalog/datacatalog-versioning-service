package edu.bbte.projectbluebook.datacatalog.versioning.model.dto;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.time.OffsetDateTime;
import org.openapitools.jackson.nullable.JsonNullable;
import java.io.Serializable;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * 
 */
@ApiModel(description = "")

public class ContentResponse  implements Serializable {
  private static final long serialVersionUID = 1L;

  @JsonProperty("name")
  private String name;

  @JsonProperty("lastModified")
  @org.springframework.format.annotation.DateTimeFormat(iso = org.springframework.format.annotation.DateTimeFormat.ISO.DATE_TIME)
  private OffsetDateTime lastModified;

  @JsonProperty("size")
  private Long size;

  public ContentResponse name(String name) {
    this.name = name;
    return this;
  }

  /**
   * The name of the blob in the container.
   * @return name
  */
  @ApiModelProperty(required = true, value = "The name of the blob in the container.")
  @NotNull


  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public ContentResponse lastModified(OffsetDateTime lastModified) {
    this.lastModified = lastModified;
    return this;
  }

  /**
   * The date when the blob was last modified.
   * @return lastModified
  */
  @ApiModelProperty(required = true, value = "The date when the blob was last modified.")
  @NotNull

  @Valid

  public OffsetDateTime getLastModified() {
    return lastModified;
  }

  public void setLastModified(OffsetDateTime lastModified) {
    this.lastModified = lastModified;
  }

  public ContentResponse size(Long size) {
    this.size = size;
    return this;
  }

  /**
   * The size of the blob in bytes.
   * minimum: 0
   * @return size
  */
  @ApiModelProperty(example = "0", value = "The size of the blob in bytes.")

@Min(0L)
  public Long getSize() {
    return size;
  }

  public void setSize(Long size) {
    this.size = size;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ContentResponse contentResponse = (ContentResponse) o;
    return Objects.equals(this.name, contentResponse.name) &&
        Objects.equals(this.lastModified, contentResponse.lastModified) &&
        Objects.equals(this.size, contentResponse.size);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, lastModified, size);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ContentResponse {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    lastModified: ").append(toIndentedString(lastModified)).append("\n");
    sb.append("    size: ").append(toIndentedString(size)).append("\n");
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

