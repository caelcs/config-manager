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

    public BuildConfig cloneBuildConfig(String sourceEnv, String targetEnv) {
        final Optional<BuildConfig> sourceBuildConfig = buildConfigRepository.findOne(sourceEnv);
        final Optional<BuildConfig> targetBuildConfig = buildConfigRepository.findOne(targetEnv);

        if (!sourceBuildConfig.isPresent() || targetBuildConfig.isPresent()) {
            throw new IllegalArgumentException();
        }

        final BuildConfig buildConfig = sourceBuildConfig.get();
        buildConfig.setEnvironment(targetEnv);
        buildConfigRepository.save(buildConfig);

        return buildConfig;
    }
}
