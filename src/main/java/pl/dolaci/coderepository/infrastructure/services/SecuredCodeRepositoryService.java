package pl.dolaci.coderepository.infrastructure.services;

import org.springframework.security.access.prepost.PreAuthorize;
import pl.dolaci.coderepository.domain.CodeRepositoryInfo;
import pl.dolaci.coderepository.services.CodeRepositoryService;
import reactor.core.publisher.Mono;

class SecuredCodeRepositoryService implements CodeRepositoryService {

    private final CodeRepositoryService service;

    public SecuredCodeRepositoryService(CodeRepositoryService service){
        this.service = service;
    }

    @Override
    @PreAuthorize("hasAnyRole('admin'")
    public Mono<CodeRepositoryInfo> findRepositoryByOwnerAndRepositoryName(String owner, String repositoryName) {
        return service.findRepositoryByOwnerAndRepositoryName(owner, repositoryName);
    }
}
