package uk.co.caeldev.config.manager.api.buildConfig;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.caeldev.spring.moprhia.MongoSettings;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class BuildConfigRepository {

    public static final String BUILD_CONFIG = "BuildConfig";
    public static final String ENVIRONMENT = "environment";

    private final Gson gson;
    private final MongoDatabase db;

    @Autowired
    public BuildConfigRepository(final MongoClient mongoClient,
                                 final Gson gson,
                                 final MongoSettings mongoSettings) {
        this.gson = gson;
        this.db = mongoClient.getDatabase(mongoSettings.getDatabase());
    }

    public void save(BuildConfig buildConfig) {
        final MongoCollection<Document> buildConfigCollection = db.getCollection(BUILD_CONFIG);
        Document document = Document.parse(gson.toJson(buildConfig));
        buildConfigCollection.insertOne(document);
    }

    public Optional<BuildConfig> findOne(String env) {
        final Document buildConfigResult = db.getCollection(BUILD_CONFIG).find(eq(ENVIRONMENT, env)).first();
        if (Objects.isNull(buildConfigResult)) {
            return Optional.empty();
        }
        final BuildConfig buildConfig = gson.fromJson(buildConfigResult.toJson(), BuildConfig.class);
        return Optional.ofNullable(buildConfig);
    }

    public void delete(String env) {
        db.getCollection(BUILD_CONFIG).deleteOne(eq(ENVIRONMENT, env));
    }

    public Optional<BuildConfig> findOneAndUpdate(String env,
                                                  final BuildConfig buildConfig) {

        final List<Bson> updates = buildConfig.getAttributes().entrySet()
                .stream()
                .map(entrySet -> set(entrySet.getKey(), entrySet.getValue()))
                .collect(Collectors.toList());

        final Document buildConfigDoc = db.getCollection(BUILD_CONFIG).findOneAndUpdate(eq(ENVIRONMENT, env), combine(updates));

        if (Objects.isNull(buildConfigDoc)) {
            return Optional.empty();
        }
        return Optional.of(gson.fromJson(buildConfigDoc.toJson(), BuildConfig.class));
    }
}
