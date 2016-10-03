package uk.co.caeldev.config.manager.api.buildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
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
}
