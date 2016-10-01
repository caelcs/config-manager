package uk.co.caeldev.config.manager.api.customSettings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static uk.org.fyodor.generators.RDG.map;
import static uk.org.fyodor.generators.RDG.string;

@RunWith(MockitoJUnitRunner.class)
public class CustomSettingControllerTest {

    private CustomSettingController customSettingController;

    @Mock
    private CustomSettingService customSettingService;

    @Before
    public void testee() {
        customSettingController = new CustomSettingController(customSettingService);
    }

    @Test
    public void shouldGetOneCustomSetting() {
        // Given
        String env = string().next();
        String key = string().next();

        //And
        String expectedValue = string().next();
        given(customSettingService.getOne(env, key)).willReturn(Optional.of(expectedValue));

        // When
        final ResponseEntity<String> responseEntity = customSettingController.getOne(env, key);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).isEqualTo(expectedValue);
    }

    @Test
    public void shouldGetNoneWhenCustomSettingDoesNotExists() {
        // Given
        String env = string().next();
        String key = string().next();

        //And
        given(customSettingService.getOne(env, key)).willReturn(Optional.empty());

        // When
        final ResponseEntity<String> responseEntity = customSettingController.getOne(env, key);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }

    @Test
    public void shouldGetGroupOfValues() {
        // Given
        String env = string().next();

        //And
        final Map<String, String> expectedValues = map(string(), string()).next();
        given(customSettingService.getCredentials(env)).willReturn(Optional.of(expectedValues));

        // When
        final ResponseEntity<Map> responseEntity = customSettingController.getCredentials(env);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        assertThat(responseEntity.getBody()).isNotNull();
        assertThat(responseEntity.getBody()).containsAllEntriesOf(expectedValues);
    }

    @Test
    public void shouldReturnNotFoundWhenThereIsNoConfig() {
        // Given
        String env = string().next();

        //And
        given(customSettingService.getCredentials(env)).willReturn(Optional.empty());

        // When
        final ResponseEntity<Map> responseEntity = customSettingController.getCredentials(env);

        // Then
        assertThat(responseEntity.getStatusCode()).isEqualTo(NOT_FOUND);
        assertThat(responseEntity.getBody()).isNull();
    }
}