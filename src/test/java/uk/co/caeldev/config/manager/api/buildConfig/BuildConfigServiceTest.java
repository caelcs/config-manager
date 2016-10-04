package uk.co.caeldev.config.manager.api.buildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.caeldev.config.manager.api.buildConfig.tests.BuildConfigBuilder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static uk.co.caeldev.config.manager.api.buildConfig.tests.BuildConfigBuilder.buildConfigBuilder;
import static uk.org.fyodor.generators.RDG.string;

@RunWith(MockitoJUnitRunner.class)
public class BuildConfigServiceTest {

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
}