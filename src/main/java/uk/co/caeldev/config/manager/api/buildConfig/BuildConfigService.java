package uk.co.caeldev.config.manager.api.buildConfig;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class BuildConfigService {

    private final BuildConfigRepository buildConfigRepository;

    @Autowired
    public BuildConfigService(final BuildConfigRepository buildConfigRepository) {
        this.buildConfigRepository = buildConfigRepository;
    }

    public Optional<BuildConfig> getOne(String env) {
        return buildConfigRepository.findOne(env);
    }
}
