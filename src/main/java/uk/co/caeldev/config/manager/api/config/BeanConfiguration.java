package uk.co.caeldev.config.manager.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.co.caeldev.config.manager.api.buildConfig.BuildConfigService;
import uk.co.caeldev.config.manager.api.customSettings.CustomSettingService;

@Configuration
public class BeanConfiguration {

    @Bean
    public CustomSettingService customSettingService() {
        return new CustomSettingService();
    }

    @Bean
    public BuildConfigService buildConfigService() {
        return new BuildConfigService();
    }
}
