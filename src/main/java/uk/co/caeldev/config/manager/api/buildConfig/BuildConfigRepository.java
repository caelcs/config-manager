package uk.co.caeldev.config.manager.api.buildConfig;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.caeldev.spring.moprhia.MongoSettings;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.mongodb.client.model.Filters.eq;

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

    public void dropCollection() {
        db.getCollection(BUILD_CONFIG).drop();
    }

    public void save(BuildConfig buildConfig) {
        final MongoCollection<Document> buildConfigCollection = db.getCollection(BUILD_CONFIG);
        Document document = Document.parse(gson.toJson(buildConfig));
        buildConfigCollection.insertOne(document);
    }

    public void save(List<BuildConfig> buildConfigs) {
        final MongoCollection<Document> buildConfigCollection = db.getCollection(BUILD_CONFIG);
        buildConfigs.stream().map(doc -> Document.parse(gson.toJson(doc))).forEach(buildConfigCollection::insertOne);
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

        final Document document = Document.parse(gson.toJson(buildConfig));

        final UpdateResult updateResult = db.getCollection(BUILD_CONFIG).updateOne(eq(ENVIRONMENT, env), new Document("$set", document));

        if (updateResult.getModifiedCount() == 0L) {
            return Optional.empty();
        }

        return findOne(env);
    }

    public List<BuildConfig> findAll() {
        List<Document> documents = Lists.newArrayList(db.getCollection(BUILD_CONFIG).find().iterator());
        return documents.stream().map(doc -> gson.fromJson(doc.toJson(), BuildConfig.class)).collect(Collectors.toList());
    }
}
