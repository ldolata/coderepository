package pl.dolaci.coderepository.infrastructure.api;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.Base64Utils;
import pl.dolaci.coderepository.CoderRepositoryApplication;
import pl.dolaci.coderepository.domain.CodeRepositoryInfo;
import pl.dolaci.coderepository.services.CodeRepositoryService;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.mockito.Mockito.times;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = CodeRepositoryController.class)
class CodeRepositoryControllerTest {

    @MockBean
    private CodeRepositoryService service;

    @Autowired
    private WebTestClient webClient;

    @Test
    void whenGettingExistingRepository() {

        String owner = "rust-lang";
        String repositoryName = "book";

        CodeRepositoryInfo info = CodeRepositoryInfo.builder().
                fullName("rust-lang/book").
                description("The Rust Programming Language").
                cloneUrl("https://github.com/rust-lang/book.git").
                stars(10207).createdAt(LocalDateTime.parse("2015-12-11T22:49:49")).build();

        Mockito.when(service.findRepositoryByOwnerAndRepositoryName(owner, repositoryName)).
                thenReturn(Mono.just(info));

        webClient.get()
                .uri("/repositories/{owner}/{repositoryName}", owner, repositoryName)
                .header("Authorization", "Basic " + Base64Utils
                        .encodeToString(("admin" + ":" + "admin").getBytes(StandardCharsets.UTF_8)))
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(service, times(1)).findRepositoryByOwnerAndRepositoryName(owner, repositoryName);
    }

    @Test
    void whenRepositoryNotExists() {

        String owner = "rust-lang";
        String repositoryName = "book";

        Mockito.when(service.findRepositoryByOwnerAndRepositoryName(owner, repositoryName)).
                thenReturn(Mono.empty());

        webClient.get()
                .uri("/repositories/{owner}/{repositoryName}", owner, repositoryName)
                .header("Authorization", "Basic " + Base64Utils
                        .encodeToString(("admin" + ":" + "admin").getBytes(StandardCharsets.UTF_8)))
                .exchange()
                .expectStatus().isOk();

        Mockito.verify(service, times(1)).findRepositoryByOwnerAndRepositoryName(owner, repositoryName);
    }

    @Test
    void whenInvalidArguments() {
        webClient.get().uri("/repositories/{owner}/{repositoryName}", "test", "")
                .exchange()
                .expectStatus().is4xxClientError();

        webClient.get().uri("/repositories/{owner}/{repositoryName}", "", "test")
                .exchange()
                .expectStatus().is4xxClientError();

        webClient.get().uri("/repositories/{owner}/{repositoryName}", null, null)
                .exchange()
                .expectStatus().is4xxClientError();
    }
}
