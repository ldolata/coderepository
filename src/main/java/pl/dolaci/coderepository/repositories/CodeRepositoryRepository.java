package pl.dolaci.coderepository.repositories;

import pl.dolaci.coderepository.domain.CodeRepositoryInfo;
import reactor.core.publisher.Mono;

public interface CodeRepositoryRepository {
    Mono<CodeRepositoryInfo> findByOwnerAndRepositoryName(String owner, String repositoryName);
}
