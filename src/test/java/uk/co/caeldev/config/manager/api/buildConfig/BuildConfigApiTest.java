package uk.co.caeldev.config.manager.api.buildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import uk.co.caeldev.config.manager.api.BaseIntegrationTest;
import uk.co.caeldev.config.manager.api.buildConfig.tests.BuildConfigBuilder;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static uk.co.caeldev.config.manager.api.buildConfig.tests.BuildConfigBuilder.buildConfigBuilder;

@RunWith(SpringRunner.class)
public class BuildConfigApiTest extends BaseIntegrationTest {

    @Autowired
    private BuildConfigRepository buildConfigRepository;

    @Test
    public void shouldGetOneBuildConfigForEnvironment() {

        given()
            .port(serverPort)
            .get("/buildconfigs/as")
        .then()
            .assertThat()
                .statusCode(equalTo(NOT_FOUND.value()));
    }
        
    @Test
    public void shouldGetOneBuildConfig() throws Exception {
        //Given
        final BuildConfig buildConfig = buildConfigBuilder().build();
        buildConfigRepository.save(buildConfig);

        given()
            .port(serverPort)
            .get(String.format("/buildconfigs/%s", buildConfig.getEnvironment()))
        .then()
            .assertThat()
                .statusCode(equalTo(OK.value()));
    }
}
