package uk.co.caeldev.config.manager.api.config;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class MongoConfigTest {

    @Bean
    public MongoClient mongoClient() {
        return new Fongo("Test Database").getMongo();
    }
}
