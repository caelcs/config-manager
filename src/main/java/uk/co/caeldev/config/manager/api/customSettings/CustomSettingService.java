package uk.co.caeldev.config.manager.api.customSettings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.*;
import java.util.stream.Collectors;

public class CustomSettingService {

    public static final String NAMESPACE_SEPARATOR = ":";
    public static final String ONE_VALUE = "%s:%s";
    public static final String CREDENTIALS_PREFIX = "credentials";
    public static final String CREDENTIAL_KEYS = "%s:" + CREDENTIALS_PREFIX + ".*";
    public static final String ALL_KEYS_BY_ENV = "%s:*";

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

    public boolean publishAll(String env) {
        final Set<String> allKeys = stringRedisTemplate.keys(String.format(ALL_KEYS_BY_ENV, env));
        final Map<String, String> keyValuePairs = allKeys.stream()
                .filter(key -> key.contains(CREDENTIALS_PREFIX))
                .map(this::removeNameSpace)
                .collect(Collectors.toMap(key -> key,
                        key -> stringRedisTemplate.opsForValue()
                                .get(String.format(ONE_VALUE, env, key))));


        //Generate Json
        //Invoke api
        return false;
    }

    private String removeNameSpace(String key) {
        final String[] split = key.split(NAMESPACE_SEPARATOR);
        return split[1];
    }
}
