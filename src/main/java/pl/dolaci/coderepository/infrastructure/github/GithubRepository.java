package pl.dolaci.coderepository.infrastructure.github;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import pl.dolaci.coderepository.domain.CodeRepositoryInfo;
import pl.dolaci.coderepository.repositories.CodeRepositoryRepository;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Slf4j
public class GithubRepository implements CodeRepositoryRepository {
    private final WebClient webClient;

    public GithubRepository(String url, String token) {
        if (StringUtils.isAnyBlank(url, token)) {
            throw new IllegalArgumentException("Invalid url or token to repository");
        }
        webClient = WebClient.builder()
                .baseUrl(url)
                .defaultHeaders((h) -> {
                    h.setBearerAuth(token);
                    h.add(HttpHeaders.ACCEPT, "application/vnd.github+json");
                }).build();
    }

    @Override
    public Mono<CodeRepositoryInfo> findByOwnerAndRepositoryName(@NotBlank String owner, @NotBlank String repositoryName) {
        return webClient.get().
                uri("/repos/{owner}/{repositoryName}", owner, repositoryName).
                retrieve().
                bodyToMono(GithubCodeRepositoryInfo.class).
                onErrorResume(WebClientResponseException.class, ex->
                        ex.getRawStatusCode()==404 ? Mono.empty(): Mono.error(ex)).
                map(this::map);
    }

    @Getter
    @NoArgsConstructor
    private static class GithubCodeRepositoryInfo {
        @JsonProperty("full_name")
        String fullName;
        @JsonProperty("clone_url")
        String cloneUrl;
        String description;
        @JsonProperty("watchers_count")
        Integer stars;
        @JsonProperty("created_at")
        LocalDateTime createdAt;
    }


    private CodeRepositoryInfo map(GithubCodeRepositoryInfo githubRepository) {
        return CodeRepositoryInfo.builder().
                fullName(githubRepository.fullName).
                description(githubRepository.description).
                cloneUrl(githubRepository.cloneUrl).
                stars(githubRepository.stars).
                createdAt(githubRepository.createdAt).build();
    }
}
