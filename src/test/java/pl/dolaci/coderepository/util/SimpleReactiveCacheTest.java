package pl.dolaci.coderepository.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.dolaci.coderepository.domain.CodeRepositoryInfo;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

@SpringBootTest
class SimpleReactiveCacheTest {

    @Autowired
    ReactiveCache<String, CodeRepositoryInfo> cache;

    @Test
    public void whenCacheIsWorking() throws Exception {
        CodeRepositoryInfo info = CodeRepositoryInfo.builder().
                fullName("rust-lang/book").
                description("The Rust Programming Language").
                cloneUrl("https://github.com/rust-lang/book.git").
                stars(10207).createdAt(LocalDateTime.parse("2015-12-11T22:49:49")).build();

        StepVerifier.create(cache.getAndCache("test", Mono.just(info)))
                .expectNext(info)
                .verifyComplete();

        StepVerifier.create(cache.get("test")).expectNext(info)
                .verifyComplete();

    }
}
