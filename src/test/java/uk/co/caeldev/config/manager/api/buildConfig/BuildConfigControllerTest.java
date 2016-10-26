package uk.co.caeldev.config.manager.api.buildConfig;

import com.google.common.collect.ImmutableMap;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.co.caeldev.config.manager.api.buildConfig.tests.BuildConfigBuilder;
import uk.org.fyodor.generators.Generator;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.http.HttpStatus.*;
import static uk.co.caeldev.config.manager.api.buildConfig.BuildConfigController.SOURCE_ENV_PARAM;
import static uk.co.caeldev.config.manager.api.buildConfig.BuildConfigController.TARGET_ENV_PARAM;
import static uk.co.caeldev.config.manager.api.buildConfig.tests.BuildConfigBuilder.buildConfigBuilder;
import static uk.org.fyodor.generators.RDG.list;
import static uk.org.fyodor.generators.RDG.string;

@RunWith(MockitoJUnitRunner.class)
public class BuildConfigControllerTest {

    private BuildConfigController buildConfigController;

    @Mock
    private BuildConfigService buildConfigService;

    @Before
    public void testee() {
        buildConfigController = new BuildConfigController(buildConfigService);
    }

    @Test
    public void shouldGetBuildConfigForEnvironment() {
        // Given
        String env = string().next();

        //And
        final BuildConfig expectedBuildConfig = buildConfigBuilder().build();
        given(buildConfigService.getOne(env)).willReturn(Optional.of(expectedBuildConfig));

        // When
        final ResponseEntity<BuildConfig> response = buildConfigController.getBuildConfig(env);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo(expectedBuildConfig);
    }

    @Test
    public void shouldFailWhenConfigDoesNotExists() {
        // Given
        String env = string().next();

        //And
        given(buildConfigService.getOne(env)).willReturn(Optional.empty());

        // When
        final ResponseEntity<BuildConfig> response = buildConfigController.getBuildConfig(env);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }
    
    @Test
    public void shouldCloneBuildConfig() throws Exception {
        //Given
        final String sourceEnv = string().next();
        final String targetEnv = string().next();
        final Map<String, String> request = ImmutableMap.of(SOURCE_ENV_PARAM, sourceEnv, TARGET_ENV_PARAM, targetEnv);

        //And
        final BuildConfig expectedBuildConfig = buildConfigBuilder().build();
        given(buildConfigService.cloneBuildConfig(sourceEnv, targetEnv)).willReturn(expectedBuildConfig);

        final ResponseEntity<BuildConfig> response = buildConfigController.cloneBuildConfig(request);
         
        //Then
        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        assertThat(response.getBody()).isEqualTo(expectedBuildConfig);
    }

    @Test
    public void shouldFailCloneSourceBuildConfigDoesNotExists() throws Exception {
        //Given
        final String sourceEnv = string().next();
        final String targetEnv = string().next();
        final Map<String, String> request = ImmutableMap.of(SOURCE_ENV_PARAM, sourceEnv, TARGET_ENV_PARAM, targetEnv);

        //And
        given(buildConfigService.cloneBuildConfig(sourceEnv, targetEnv)).willThrow(IllegalArgumentException.class);

        //When
        final ResponseEntity<BuildConfig> response = buildConfigController.cloneBuildConfig(request);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
    }

    @Test
    public void shouldCreateANewBuildConfig() {
        // Given
        final BuildConfig buildConfig = buildConfigBuilder().build();

        // When
        final ResponseEntity<BuildConfig> response = buildConfigController.create(buildConfig);

        // Then
        verify(buildConfigService).create(buildConfig);
        assertThat(response.getStatusCode()).isEqualTo(CREATED);
        assertThat(response.getBody()).isEqualTo(buildConfig);
    }

    @Test
    public void shouldFailCreateWhenBuildConfigAlreadyExists() {
        // Given
        final BuildConfig buildConfig = buildConfigBuilder().build();

        doThrow(new IllegalArgumentException()).when(buildConfigService).create(buildConfig);

        // When
        final ResponseEntity<BuildConfig> response = buildConfigController.create(buildConfig);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
    }
    
    @Test
    public void shouldDeleteBuildConfig() {
        // Given
        String env = string().next();

        // When
        final ResponseEntity response = buildConfigController.delete(env);
        
        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(buildConfigService).deleteBuildConfig(env);
    }

    @Test
    public void shouldFailDeleteWhenBuildConfigDoesNotExist() {
        // Given
        String env = string().next();

        //And
        doThrow(IllegalArgumentException.class).when(buildConfigService).deleteBuildConfig(env);

        // When
        final ResponseEntity response = buildConfigController.delete(env);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void shouldFailUpdateBuildConfigWhenDoesNotExist() {
        // Given
        final BuildConfig buildConfig = buildConfigBuilder().build();
        String env = string().next();

        //And
        given(buildConfigService.update(env, buildConfig)).willReturn(Optional.empty());

        // When
        final ResponseEntity<BuildConfig> response = buildConfigController.update(env, buildConfig);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(NOT_FOUND);
    }

    @Test
    public void shouldUpdateBuildConfig() {
        // Given
        final BuildConfig buildConfig = buildConfigBuilder().build();
        String env = string().next();

        //And
        final BuildConfig expectedBuildConfigUpdated = buildConfigBuilder().environment(env).build();
        given(buildConfigService.update(env, buildConfig)).willReturn(Optional.of(expectedBuildConfigUpdated));

        // When
        final ResponseEntity<BuildConfig> response = buildConfigController.update(env, buildConfig);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isEqualTo(expectedBuildConfigUpdated);
    }

    @Test
    public void shouldGetAllBuildConfigs() {
        // Given
        List<BuildConfig> expectedBuildConfigs = list(() -> buildConfigBuilder().build()).next();

        //And
        given(buildConfigService.getAll()).willReturn(expectedBuildConfigs);

        // When
        final ResponseEntity<List<BuildConfig>> result = buildConfigController.getAllBuildConfig();

        // Then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(result.getBody()).containsAll(expectedBuildConfigs);
    }
}
