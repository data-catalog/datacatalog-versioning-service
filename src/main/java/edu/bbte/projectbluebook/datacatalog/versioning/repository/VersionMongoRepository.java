package edu.bbte.projectbluebook.datacatalog.versioning.repository;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class VersionMongoRepository {

    private static String connection = "mongodb+srv://m001-student:m001-mongodb-basics"
            + "@cluster0.dlhll.mongodb.net/DataCatalog?retryWrites=true&w=majority";
    private static MongoClientURI uri = new MongoClientURI(connection);
    private static MongoClient mongoClient = new MongoClient(uri);
    private static MongoDatabase database = mongoClient.getDatabase("DataCatalog");
    private static MongoCollection<Document> assets = database.getCollection("Assets");
    private static MongoCollection<Document> versions = database.getCollection("Versions");

    public int isOwnerOfAsset(Document assetId, String uid) {
        Document asset = assets.find(assetId).first();
        if (asset == null) {
            return -1;
        }
        if (asset.getString("owner").equals(uid)) {
            return 1;
        }
        return 0;
    }

    public boolean insert(Document version) {
        try {
            Date currentTime = new Date();
            version.append("createdAt", currentTime);
            versions.insertOne(version);
        } catch (MongoException e) {
            return false;
        }
        return true;
    }

    public Document delete(String assetId, String name) {
        Document filter = new Document();
        filter.append("assetId", assetId);
        filter.append("name", name);
        return versions.findOneAndDelete(filter);
    }

    public boolean versionExists(String assetId, String name) {
        Document version = new Document("assetId", assetId);
        version.append("name", name);
        Document exists = versions.find(version).first();
        return exists != null;

    }

    public Document findAssetVersion(String assetId, String name) {
        Document version = new Document("assetId", assetId);
        version.append("name", name);
        return versions.find(version).first();
    }

    public FindIterable<Document> findVersions(String assetId) {
        Document version = new Document("assetId", assetId);
        return versions.find(version);
    }

    public Document getAssetData(Document id) {
        return assets.find(id).first();
    }
}
