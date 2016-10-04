package uk.co.caeldev.config.manager.api.config;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.caeldev.config.manager.api.buildConfig.BuildConfigRepository;
import uk.co.caeldev.config.manager.api.buildConfig.BuildConfigService;
import uk.co.caeldev.spring.moprhia.MongoSettings;

@Configuration
public class BeanConfiguration {

    @Bean
    public Gson gson() {
        return new Gson();
    }

    @Bean
    public BuildConfigRepository buildConfigRepository(final MongoClient mongoClient,
                                                       final Gson gson,
                                                       final MongoSettings mongoSettings){
        return new BuildConfigRepository(mongoClient, gson, mongoSettings);
    }

    @Bean
    public BuildConfigService buildConfigService(final BuildConfigRepository buildConfigRepository) {
        return new BuildConfigService(buildConfigRepository);
    }
}
