package pl.dolaci.coderepository.infrastructure.github;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.dolaci.coderepository.domain.CodeRepositoryInfo;

import java.time.LocalDateTime;

public class GithubRepositoryTestIT {
    private GithubRepository underTest;

    @BeforeEach
    public void before() {
        String token = System.getenv("GITHUB_TOKEN");
        String url="https://api.github.com";
        underTest = new GithubRepository(url, token);
    }
    @Test
    public void  whenGettingPublicRepo() {
      String token = System.getenv("GITHUB_TOKEN");
      CodeRepositoryInfo info =  underTest.findByOwnerAndRepositoryName("rust-lang", "book").block();
      Assertions.assertNotNull(info);
      Assertions.assertEquals(info.getFullName(), "rust-lang/book");
      Assertions.assertEquals(info.getDescription(), "The Rust Programming Language");
      Assertions.assertNotNull(info.getCloneUrl());
      Assertions.assertNotNull(info.getCreatedAt());
      Assertions.assertNotNull(info.getStars());

      System.out.println(info);
    }

    @Test
    public void whenGettingNotExistingRepo() {
        Assertions.assertTrue(underTest.findByOwnerAndRepositoryName("xxxx", "xxx").blockOptional().isEmpty());
    }
}
