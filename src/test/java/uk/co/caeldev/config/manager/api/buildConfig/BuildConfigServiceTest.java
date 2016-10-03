package uk.co.caeldev.config.manager.api.buildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.caeldev.config.manager.api.buildConfig.tests.BuildConfigBuilder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.org.fyodor.generators.RDG.string;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BuildConfigServiceTest {

    @Autowired
    private BuildConfigService buildConfigService;

    @Test
    public void shouldGetOneBuildConfig() throws Exception {
        //Given
        final String env = string().next();

        //When
        final Optional<BuildConfig> result = buildConfigService.getOne(env);

        //Then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isNotNull();
    }

    @Test
    public void shouldPersistOneBuildConfig() throws Exception {
        //Given

        //When
        buildConfigService.persistBuildConfig(BuildConfigBuilder.buildConfigBuilder().build());

        //Then
    }
}