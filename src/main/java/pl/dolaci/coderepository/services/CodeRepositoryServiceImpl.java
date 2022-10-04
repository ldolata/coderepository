package pl.dolaci.coderepository.services;

import pl.dolaci.coderepository.domain.CodeRepositoryInfo;
import pl.dolaci.coderepository.repositories.CodeRepositoryRepository;
import reactor.core.publisher.Mono;


public class CodeRepositoryServiceImpl implements CodeRepositoryService{

    private final CodeRepositoryRepository repository;

    public CodeRepositoryServiceImpl(CodeRepositoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Mono<CodeRepositoryInfo> findRepositoryByOwnerAndRepositoryName(String owner, String repositoryName) {
        return repository.findByOwnerAndRepositoryName(owner, repositoryName);
    }
}
