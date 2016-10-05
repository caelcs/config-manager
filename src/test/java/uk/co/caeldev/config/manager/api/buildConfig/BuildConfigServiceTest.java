package uk.co.caeldev.config.manager.api.buildConfig;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static uk.co.caeldev.config.manager.api.buildConfig.BuildConfigService.*;
import static uk.co.caeldev.config.manager.api.buildConfig.tests.BuildConfigBuilder.buildConfigBuilder;
import static uk.org.fyodor.generators.RDG.string;

@RunWith(MockitoJUnitRunner.class)
public class BuildConfigServiceTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private BuildConfigService buildConfigService;

    @Mock
    private BuildConfigRepository buildConfigRepository;

    @Before
    public void testee() {
        buildConfigService = new BuildConfigService(buildConfigRepository);
    }

    @Test
    public void shouldGetOneBuildConfig() throws Exception {
        //Given
        final String env = string().next();

        //And
        Optional<BuildConfig> expectedBuildConfig = Optional.of(buildConfigBuilder()
                .environment(env)
                .build());
        given(buildConfigRepository.findOne(env)).willReturn(expectedBuildConfig);

        //When
        final Optional<BuildConfig> result = buildConfigService.getOne(env);

        //Then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isNotNull();
        assertThat(result.get()).isEqualTo(expectedBuildConfig.get());
    }

    @Test
    public void shouldNotFoundBuildConfigWhenEnvironmentIsNotValid() throws Exception {
        //Given
        final String env = string().next();

        //And
        given(buildConfigRepository.findOne(env)).willReturn(Optional.empty());

        //When
        final Optional<BuildConfig> result = buildConfigService.getOne(env);

        //Then
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void shouldCloneBuildConfig() throws Exception {
        //Given
        final String sourceEnv = string().next();
        final String targetEnv = string().next();

        //And
        final BuildConfig sourceBuildConfig = buildConfigBuilder().environment(sourceEnv).build();
        given(buildConfigRepository.findOne(sourceEnv)).willReturn(Optional.of(sourceBuildConfig));

        //And
        given(buildConfigRepository.findOne(targetEnv)).willReturn(Optional.empty());

        //When
        final BuildConfig targetBuildConfig = buildConfigService.cloneBuildConfig(sourceEnv, targetEnv);

        //Then
        sourceBuildConfig.setEnvironment(targetEnv);
        assertThat(targetBuildConfig).isNotNull();
        assertThat(targetBuildConfig).isEqualTo(sourceBuildConfig);
        verify(buildConfigRepository).save(sourceBuildConfig);
    }

    @Test
    public void shouldThrowExceptionWhenCloneAndTargetBuildConfigDoesExist() throws Exception {
        //Given
        final String sourceEnv = string().next();
        final String targetEnv = string().next();

        //And
        final BuildConfig sourceBuildConfig = buildConfigBuilder().environment(sourceEnv).build();
        given(buildConfigRepository.findOne(sourceEnv)).willReturn(Optional.of(sourceBuildConfig));

        //And
        final BuildConfig targetBuildConfig = buildConfigBuilder().environment(targetEnv).build();
        given(buildConfigRepository.findOne(targetEnv)).willReturn(Optional.of(targetBuildConfig));

        //Expect
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(TARGET_ENVIRONMENT_MUST_NOT_EXIST);

        //When
        buildConfigService.cloneBuildConfig(sourceEnv, targetEnv);
    }

    @Test
    public void shouldThrowExceptionWhenCloneAndSourceBuildConfigDoesNotExist() throws Exception {
        //Given
        final String sourceEnv = string().next();
        final String targetEnv = string().next();

        //And
        given(buildConfigRepository.findOne(sourceEnv)).willReturn(Optional.empty());

        //Expect
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(SOURCE_ENVIRONMENT_MUST_EXIST);

        //When
        buildConfigService.cloneBuildConfig(sourceEnv, targetEnv);
    }

    @Test
    public void shouldCreateBuildConfig() {
        // Given
        final BuildConfig buildConfig = buildConfigBuilder().build();

        //And
        given(buildConfigRepository.findOne(buildConfig.getEnvironment())).willReturn(Optional.empty());

        // When
        buildConfigService.create(buildConfig);

        //Then
        verify(buildConfigRepository).save(buildConfig);
    }

    @Test
    public void shouldFailCreateBuildConfigWhenAlreadyExists() {
        // Given
        String env = string().next();
        final BuildConfig buildConfig = buildConfigBuilder().environment(env).build();

        //And
        final BuildConfig expectedExistingBuildConfig = buildConfigBuilder().environment(env).build();
        given(buildConfigRepository.findOne(env)).willReturn(Optional.of(expectedExistingBuildConfig));

        //Expect
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(BUILD_CONFIGURATION_ALREADY_EXISTS);

        // When
        buildConfigService.create(buildConfig);

        //Then
        verify(buildConfigRepository,never()).save(buildConfig);
    }
    
    @Test
    public void shouldDeleteBuildConfig() {
        // Given
        String env = string().next();

        //And
        final BuildConfig expectedExistingBuildConfig = buildConfigBuilder().environment(env).build();
        given(buildConfigRepository.findOne(env)).willReturn(Optional.of(expectedExistingBuildConfig));

        // When
        buildConfigService.deleteBuildConfig(env);

        //Then
        verify(buildConfigRepository).delete(env);
    }

    @Test
    public void shouldFailDeleteBuildConfigWhenDoesNotExist() {
        // Given
        String env = string().next();

        //And
        given(buildConfigRepository.findOne(env)).willReturn(Optional.empty());

        //Expect
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage(BUILD_CONFIG_DOES_NOT_EXIST_DELETION_ABORTED);

        // When
        buildConfigService.deleteBuildConfig(env);

        //Then
        verify(buildConfigRepository, never()).delete(env);
    }
}