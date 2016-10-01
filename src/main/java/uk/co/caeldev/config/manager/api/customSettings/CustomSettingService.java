package uk.co.caeldev.config.manager.api.customSettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomSettingService {

    public static final String ONE_VALUE = "%s:%s";
    public static final String CREDENTIAL_KEYS = "%s:credentials.*";
    private final StringRedisTemplate stringRedisTemplate;

    @Autowired
    public CustomSettingService(final StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    public Optional<String> getOne(String env, String key) {
        String value = stringRedisTemplate.opsForValue().get(String.format(ONE_VALUE, env, key));
        return Optional.ofNullable(value);
    }

    public Optional<Map<String, String>> getCredentials(String env) {
        final Set<String> keys = stringRedisTemplate.keys(String.format(CREDENTIAL_KEYS, env));

        if (Objects.isNull(keys)) {
            return Optional.empty();
        }

        final Map<String, String> result = keys.stream()
                .collect(Collectors.toMap(
                        key -> key,
                        key -> stringRedisTemplate.opsForValue().get(key)));

        return Optional.of(result);
    }
}
