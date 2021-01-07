package edu.bbte.projectbluebook.datacatalog.versioning.helpers;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobContainerClientBuilder;
import edu.bbte.projectbluebook.datacatalog.versioning.model.ContentResponse;
import org.bson.Document;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Utility {

    public static ContentResponse convertDocToContentResp(Document doc) {
        ContentResponse contentResponse = new ContentResponse();
        contentResponse.setLastModified(doc
                .getDate("lastModified")
                .toInstant()
                .atOffset(ZoneOffset.UTC)
        );
        contentResponse.setName(doc.getString("name"));
        contentResponse.setSize(doc.getLong("size"));
        return contentResponse;
    }

    public static List<Document> getVersionContent(Document asset) {
        Document location = (Document) asset.get("location");
        Document parameters = (Document) location.get("parameters");

        BlobContainerClient container = new BlobContainerClientBuilder()
                .endpoint(parameters.getString("accountUrl"))
                .sasToken(parameters.getString("sasToken"))
                .containerName(parameters.getString("containerName"))
                .buildClient();

        return container
                .listBlobs()
                .stream()
                .map(blob -> new Document()
                        .append("name", blob.getName())
                        .append("lastModified", new Date(blob.getProperties().getLastModified().toInstant().toEpochMilli()))
                        .append("size", blob.getProperties().getContentLength()))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
