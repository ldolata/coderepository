package pl.dolaci.coderepository.services;

import pl.dolaci.coderepository.domain.CodeRepositoryInfo;
import reactor.core.publisher.Mono;

public interface CodeRepositoryService {
    Mono<CodeRepositoryInfo> findRepositoryByOwnerAndRepositoryName(String owner, String repositoryName);
}
