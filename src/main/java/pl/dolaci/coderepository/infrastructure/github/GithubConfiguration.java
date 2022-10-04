package pl.dolaci.coderepository.infrastructure.github;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableConfigurationProperties(GithubConfiguration.GithubRestConfigurationProperties.class)
class GithubConfiguration {

    @Bean
    public GithubRepository githubRestRepository (GithubRestConfigurationProperties properties) {
        return new GithubRepository(properties.getUrl(), properties.getToken());
    };

   @ConfigurationProperties(prefix = "github.repository")
   @Getter
   @Setter
   static class GithubRestConfigurationProperties {
        private String url;
        private String token;
   }
}
