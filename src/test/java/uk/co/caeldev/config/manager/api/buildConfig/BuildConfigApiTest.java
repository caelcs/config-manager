package uk.co.caeldev.config.manager.api.buildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BuildConfigApiTest {

    @Test
    public void shouldGetOneBuildConfigForEnvironment() {
        // Given
    
        // When
        get("/buildconfigs/as").then().assertThat().statusCode(equalTo(NOT_FOUND.value()));
        
        // Then
    }
        

}
