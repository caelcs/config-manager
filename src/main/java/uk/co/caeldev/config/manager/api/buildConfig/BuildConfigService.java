package uk.co.caeldev.config.manager.api.buildConfig;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

public class BuildConfigService {

    public static final String SOURCE_ENVIRONMENT_MUST_EXIST = "Source Environment must EXIST.";
    public static final String TARGET_ENVIRONMENT_MUST_NOT_EXIST = "Target Environment must NOT EXIST.";
    public static final String BUILD_CONFIGURATION_ALREADY_EXISTS = "Build configuration already exists";
    public static final String BUILD_CONFIG_DOES_NOT_EXIST_DELETION_ABORTED = "Build Config does not exist. deletion aborted";
    private final BuildConfigRepository buildConfigRepository;

    @Autowired
    public BuildConfigService(final BuildConfigRepository buildConfigRepository) {
        this.buildConfigRepository = buildConfigRepository;
    }

    public Optional<BuildConfig> getOne(String env) {
        return buildConfigRepository.findOne(env);
    }

    public BuildConfig cloneBuildConfig(String sourceEnv,
                                        String targetEnv) {
        final Optional<BuildConfig> sourceBuildConfig = buildConfigRepository.findOne(sourceEnv);
        final Optional<BuildConfig> targetBuildConfig = buildConfigRepository.findOne(targetEnv);

        checkArgument(sourceBuildConfig.isPresent(), SOURCE_ENVIRONMENT_MUST_EXIST);
        checkArgument(!targetBuildConfig.isPresent(), TARGET_ENVIRONMENT_MUST_NOT_EXIST);

        final BuildConfig buildConfig = sourceBuildConfig.get();
        buildConfig.setEnvironment(targetEnv);
        buildConfigRepository.save(buildConfig);

        return buildConfig;
    }

    public void create(final BuildConfig buildConfig) {
        Optional<BuildConfig> existingBuildConfig = buildConfigRepository.findOne(buildConfig.getEnvironment());

        checkArgument(!existingBuildConfig.isPresent(), BUILD_CONFIGURATION_ALREADY_EXISTS);

        buildConfigRepository.save(buildConfig);
    }

    public void deleteBuildConfig(String env) {
        Optional<BuildConfig> existingBuildConfig = buildConfigRepository.findOne(env);
        checkArgument(existingBuildConfig.isPresent(), BUILD_CONFIG_DOES_NOT_EXIST_DELETION_ABORTED);
        buildConfigRepository.delete(env);
    }

    public Optional<BuildConfig> update(String env,
                              final BuildConfig buildConfig) {
        return buildConfigRepository.findOneAndUpdate(env, buildConfig);
    }

    public List<BuildConfig> getAll() {
        return buildConfigRepository.findAll();
    }
}
