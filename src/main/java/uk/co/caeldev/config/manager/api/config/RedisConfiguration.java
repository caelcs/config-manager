package uk.co.caeldev.config.manager.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import uk.co.caeldev.config.manager.api.customSettings.CustomSettingService;

@Configuration
@EnableRedisRepositories
public class RedisConfiguration {

    @Bean
    public RedisConnectionFactory connectionFactory(final RedisConfigurationProperties redisConfigurationProperties) {
        final JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
        jedisConnectionFactory.setHostName(redisConfigurationProperties.getHost());
        jedisConnectionFactory.setPort(redisConfigurationProperties.getPort());
        return jedisConnectionFactory;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(final RedisConnectionFactory redisConnectionFactory) {
        return new StringRedisTemplate(redisConnectionFactory);
    }

    @Bean
    public CustomSettingService customSettingService(final StringRedisTemplate stringRedisTemplate) {
        return new CustomSettingService(stringRedisTemplate);
    }
}
