package edu.bbte.projectbluebook.datacatalog.versioning.helpers;

import edu.bbte.projectbluebook.datacatalog.versioning.model.ContentResponse;
import org.bson.Document;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static ContentResponse convertDocToContentResp(Document doc) {
        ContentResponse contentResponse = new ContentResponse();
        contentResponse.setId(doc.getString("id"));
        contentResponse.setLastModified(doc
                .getDate("lastModified")
                .toInstant()
                .atOffset(ZoneOffset.UTC)
        );
        contentResponse.setName(doc.getString("name"));
        contentResponse.setSize(doc.getInteger("size"));
        return contentResponse;
    }

    public static List<Document> getVersionContent(Document asset) {
        return new ArrayList<>();
    }
}
