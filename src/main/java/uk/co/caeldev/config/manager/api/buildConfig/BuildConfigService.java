package uk.co.caeldev.config.manager.api.buildConfig;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

public class BuildConfigService {

    public static final String SOURCE_ENVIRONMENT_MUST_EXIST = "Source Environment must EXIST.";
    public static final String TARGET_ENVIRONMENT_MUST_NOT_EXIST = "Target Environment must NOT EXIST.";
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

        checkArgument(sourceBuildConfig.isPresent(), SOURCE_ENVIRONMENT_MUST_EXIST);
        checkArgument(!targetBuildConfig.isPresent(), TARGET_ENVIRONMENT_MUST_NOT_EXIST);

        final BuildConfig buildConfig = sourceBuildConfig.get();
        buildConfig.setEnvironment(targetEnv);
        buildConfigRepository.save(buildConfig);

        return buildConfig;
    }

    public void create(BuildConfig buildConfig) {

    }
}
