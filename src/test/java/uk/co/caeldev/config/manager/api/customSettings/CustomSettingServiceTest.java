package uk.co.caeldev.config.manager.api.customSettings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static uk.co.caeldev.config.manager.api.customSettings.CustomSettingService.CREDENTIAL_KEYS;
import static uk.co.caeldev.config.manager.api.customSettings.CustomSettingService.ONE_VALUE;
import static uk.org.fyodor.generators.RDG.*;

@RunWith(MockitoJUnitRunner.class)
public class CustomSettingServiceTest {

    private CustomSettingService customSettingService;

    @Mock
    private StringRedisTemplate stringRedisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;
    
    @Before
    public void testee() {
        customSettingService = new CustomSettingService(stringRedisTemplate);
    }

    @Test
    public void shouldGetOneCustomSetting() {
        // Given
        String env = string().next();
        String key = string().next();

        //And
        given(stringRedisTemplate.opsForValue()).willReturn(valueOperations);

        //And
        String expectedValue = string().next();
        given(valueOperations.get(String.format(ONE_VALUE, env, key)))
                .willReturn(expectedValue);

        // When
        Optional<String> result = customSettingService.getOne(env, key);

        // Then
        assertThat(result.isPresent()).isTrue();
        assertThat(result.get()).isEqualTo(expectedValue);
    }

    @Test
    public void shouldReturnNullWhenKeyDoesNotExists() {
        // Given
        String env = string().next();
        String key = string().next();

        //And
        given(stringRedisTemplate.opsForValue())
                .willReturn(valueOperations);

        //And
        given(valueOperations.get(String.format(ONE_VALUE, env, key)))
                .willReturn(null);

        // When
        Optional<String> result = customSettingService.getOne(env, key);

        // Then
        assertThat(result.isPresent()).isFalse();
    }

    @Test
    public void shouldGetCredentialsForEnvironment() {
        // Given
        String env = string().next();

        //And
        final Set<String> expectedKeys = set(string()).next();
        given(stringRedisTemplate.keys(String.format(CREDENTIAL_KEYS, env)))
                .willReturn(expectedKeys);

        //And
        given(stringRedisTemplate.opsForValue()).willReturn(valueOperations);

        //And
        final List<String> expectedValues = list(string(), expectedKeys.size()).next();
        final Deque<String> expectedValuesQueue = new ArrayDeque<>(expectedValues);
        expectedKeys.forEach(key ->
                given(valueOperations.get(key))
                        .willReturn(expectedValuesQueue.poll())
        );

        // When
        final Optional<Map<String, String>> credentials = customSettingService.getCredentials(env);

        // Then
        assertThat(credentials.isPresent()).isTrue();

        final Map<String, String> result = credentials.get();
        assertThat(result).isNotEmpty();
        assertThat(result).containsKeys(expectedKeys.stream().toArray(String[]::new));
        assertThat(result).containsValues(expectedValues.stream().toArray(String[]::new));
    }

    @Test
    public void shouldReturnNoCredentialsForAMissingEnvironment() {
        // Given
        String env = string().next();

        //And
        given(stringRedisTemplate.keys(String.format(CREDENTIAL_KEYS, env)))
                .willReturn(null);

        // When
        final Optional<Map<String, String>> credentials = customSettingService.getCredentials(env);

        // Then
        assertThat(credentials.isPresent()).isFalse();
    }
}