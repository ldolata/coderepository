package pl.dolaci.coderepository.infrastructure.cache.redis;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import pl.dolaci.coderepository.domain.CodeRepositoryInfo;
import pl.dolaci.coderepository.infrastructure.cache.CacheConfiguration;
import pl.dolaci.coderepository.util.ReactiveCache;

import java.time.Duration;

@Configuration
@Slf4j
@ConditionalOnProperty(prefix = "cache", name = "type", havingValue = "redis")
@EnableConfigurationProperties(RedisCacheConfiguration.RedisCacheConfigurationProperties.class)
public class RedisCacheConfiguration {

    @ConfigurationProperties(prefix="cache.redis")
    @Getter
    @Setter
    static class RedisCacheConfigurationProperties {
        private String  host;
        private Integer port;
        private Integer serverTimeoutMillis;
    }

    @Bean
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory(RedisCacheConfigurationProperties properties) {
        return new LettuceConnectionFactory(properties.getHost(), properties.getPort());
    }

    @Bean
    public ReactiveRedisOperations<String, CodeRepositoryInfo> redisCodeRepositoryOperations(ReactiveRedisConnectionFactory reactiveRedisConnectionFactory) {
        Jackson2JsonRedisSerializer<CodeRepositoryInfo> serializer = new Jackson2JsonRedisSerializer<>(CodeRepositoryInfo.class);
        RedisSerializationContext.RedisSerializationContextBuilder<String, CodeRepositoryInfo> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());
        RedisSerializationContext<String, CodeRepositoryInfo> context = builder.value(serializer).hashValue(serializer)
                .hashKey(serializer).build();
        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, context);
    }

    @Bean
    public ReactiveCache<String, CodeRepositoryInfo> redisCodeRepositoriesCache(ReactiveRedisOperations<String,
                CodeRepositoryInfo> operations, RedisCacheConfigurationProperties redisCacheConfigurationProperties,
                                                                                CacheConfiguration.CacheConfigurationProperties cacheConfigurationProperties ) {
        return new FaultTolerantRedisCache<>(operations,
                Duration.ofMinutes(cacheConfigurationProperties.getEviction()), Duration.ofMillis(redisCacheConfigurationProperties.getServerTimeoutMillis()));
    }

}

