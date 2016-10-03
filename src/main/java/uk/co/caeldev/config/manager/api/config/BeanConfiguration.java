package uk.co.caeldev.config.manager.api.config;

import com.google.gson.Gson;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.caeldev.config.manager.api.buildConfig.BuildConfigService;
import uk.co.caeldev.config.manager.api.customSettings.CustomSettingService;
import uk.co.caeldev.spring.moprhia.MongoSettings;

@Configuration
public class BeanConfiguration {

    @Bean
    public Gson gson() {
        return new Gson();
    }

    @Bean
    public CustomSettingService customSettingService() {
        return new CustomSettingService();
    }

    @Bean
    public BuildConfigService buildConfigService(final MongoClient mongoClient,
                                                 final Gson gson,
                                                 final MongoSettings mongoSettings) {
        return new BuildConfigService(mongoClient, gson, mongoSettings);
    }
}
