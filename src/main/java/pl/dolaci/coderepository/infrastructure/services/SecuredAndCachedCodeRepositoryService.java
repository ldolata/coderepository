package pl.dolaci.coderepository.infrastructure.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.security.access.prepost.PreAuthorize;
import pl.dolaci.coderepository.domain.CodeRepositoryInfo;
import pl.dolaci.coderepository.services.CodeRepositoryService;
import pl.dolaci.coderepository.util.ReactiveCache;
import reactor.core.publisher.Mono;

@Slf4j
class SecuredAndCachedCodeRepositoryService implements CodeRepositoryService {

    private final String KEY = "repositories";
    private final CodeRepositoryService service;

    private final ReactiveCache<String, CodeRepositoryInfo> cache;

    public SecuredAndCachedCodeRepositoryService(CodeRepositoryService service, ReactiveCache<String, CodeRepositoryInfo> cache) {
        this.service = service;
        this.cache = cache;
    }

    @Override
    @PreAuthorize("hasAnyRole('admin'")
    public Mono<CodeRepositoryInfo> findRepositoryByOwnerAndRepositoryName(String owner, String repositoryName) {
        return cache.getAndCache(owner + ":" + repositoryName, service.findRepositoryByOwnerAndRepositoryName(owner, repositoryName));
    }
}
