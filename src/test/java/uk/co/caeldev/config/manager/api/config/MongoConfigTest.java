package uk.co.caeldev.config.manager.api.config;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class MongoConfigTest {

    @Bean
    public MongoClient mongoClient() {
        return new Fongo("Test Database").getMongo();
    }
}
