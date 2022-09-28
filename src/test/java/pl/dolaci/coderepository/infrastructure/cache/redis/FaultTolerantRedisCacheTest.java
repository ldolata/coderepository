package pl.dolaci.coderepository.infrastructure.cache.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.test.context.ActiveProfiles;
import pl.dolaci.coderepository.domain.CodeRepositoryInfo;
import pl.dolaci.coderepository.util.ReactiveCache;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.time.LocalDateTime;


@SpringBootTest
@Slf4j
@ActiveProfiles("redis")
public class FaultTolerantRedisCacheTest {

    EmbeddedRedisServer server;

    @Value("${cache.redis.port}")
    private Integer port;

    @Autowired
    ReactiveCache<String, CodeRepositoryInfo> cache;

    @BeforeEach
    public void before() throws Exception {
        server= new EmbeddedRedisServer(port);
        server.start();
    }

    @AfterEach
    public void after() throws Exception {
        server.destroy();
    }

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

    @Test
    public void whenCacheIsNotWorking() throws Exception {
        server.destroy();
        CodeRepositoryInfo info = CodeRepositoryInfo.builder().
                fullName("rust-lang/book").
                description("The Rust Programming Language").
                cloneUrl("https://github.com/rust-lang/book.git").
                stars(10207).createdAt(LocalDateTime.parse("2015-12-11T22:49:49")).build();

        StepVerifier.create(cache.getAndCache("test", Mono.just(info)))
                .expectNext(info)
                .verifyComplete();


    }
}
