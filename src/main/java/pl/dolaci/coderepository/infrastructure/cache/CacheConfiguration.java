package pl.dolaci.coderepository.infrastructure.cache;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.dolaci.coderepository.domain.CodeRepositoryInfo;
import pl.dolaci.coderepository.util.ReactiveCache;
import pl.dolaci.coderepository.util.SimpleReactiveCache;

import java.time.Duration;

@Configuration
@EnableConfigurationProperties(CacheConfiguration.CacheConfigurationProperties.class)
public class CacheConfiguration {

    @ConfigurationProperties(prefix = "cache")
    @Getter
    @Setter
    public static class CacheConfigurationProperties {
        private String type;
        private Integer eviction;
    }
    @Bean
    @ConditionalOnProperty(prefix = "cache", name = "type", havingValue = "default")
    public ReactiveCache<String, CodeRepositoryInfo> defaultCache(CacheConfigurationProperties properties) {
        return new SimpleReactiveCache<>(Duration.ofMinutes(properties.getEviction()));
    }
}
