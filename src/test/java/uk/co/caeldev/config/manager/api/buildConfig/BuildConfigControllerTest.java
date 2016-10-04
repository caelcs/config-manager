package uk.co.caeldev.config.manager.api.buildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.*;
import static uk.co.caeldev.config.manager.api.buildConfig.BuildConfigController.SOURCE_ENV_PARAM;
import static uk.co.caeldev.config.manager.api.buildConfig.BuildConfigController.TARGET_ENV_PARAM;
import static uk.co.caeldev.config.manager.api.buildConfig.tests.BuildConfigBuilder.buildConfigBuilder;
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

        //And
        final BuildConfig expectedBuildConfig = buildConfigBuilder().build();
        given(buildConfigService.cloneBuildConfig(sourceEnv, targetEnv)).willReturn(expectedBuildConfig);

        //When
        final Map<String, String> request = new HashMap<>();
        request.put(SOURCE_ENV_PARAM, sourceEnv);
        request.put(TARGET_ENV_PARAM, targetEnv);

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
        final Map<String, String> request = new HashMap<>();
        request.put(SOURCE_ENV_PARAM, sourceEnv);
        request.put(TARGET_ENV_PARAM, targetEnv);

        //And
        given(buildConfigService.cloneBuildConfig(sourceEnv, targetEnv)).willThrow(IllegalArgumentException.class);

        //When
        final ResponseEntity<BuildConfig> response = buildConfigController.cloneBuildConfig(request);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(INTERNAL_SERVER_ERROR);
    }
}
