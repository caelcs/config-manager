package uk.co.caeldev.config.manager.api.buildConfig;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.caeldev.spring.moprhia.MongoSettings;

import java.util.Optional;

import static com.mongodb.client.model.Filters.eq;

public class BuildConfigRepository {

    private final MongoClient mongoClient;
    private final Gson gson;
    private final MongoSettings mongoSettings;

    @Autowired
    public BuildConfigRepository(final MongoClient mongoClient,
                                 final Gson gson,
                                 final MongoSettings mongoSettings) {
        this.mongoClient = mongoClient;
        this.gson = gson;
        this.mongoSettings = mongoSettings;
    }

    public void save(BuildConfig buildConfig) {
        final MongoDatabase db = mongoClient.getDatabase(mongoSettings.getDatabase());
        final MongoCollection<Document> collection = db.getCollection("BuildConfig");
        final Document document = new Document();
        document.put("environment", buildConfig.getEnvironment());
        document.put("attributes", buildConfig.getAttributes());
        collection.insertOne(document);
    }

    public Optional<BuildConfig> findOne(String env) {
        final MongoDatabase db = mongoClient.getDatabase(mongoSettings.getDatabase());
        final Document buildConfigResult = db.getCollection("BuildConfig").find(eq("environment", env)).first();
        final BuildConfig buildConfig = gson.fromJson(buildConfigResult.toJson(), BuildConfig.class);
        return Optional.ofNullable(buildConfig);
    }
}
