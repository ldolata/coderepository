package pl.dolaci.coderepository.infrastructure.services;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.dolaci.coderepository.domain.CodeRepositoryInfo;
import pl.dolaci.coderepository.repositories.CodeRepositoryRepository;
import pl.dolaci.coderepository.services.CodeRepositoryServiceImpl;
import pl.dolaci.coderepository.util.ReactiveCache;

@Configuration
public class ServicesConfiguration {
    @Bean
    public SecuredAndCachedCodeRepositoryService codeRepositoryService(CodeRepositoryRepository repository, ReactiveCache<String, CodeRepositoryInfo> operations) {
        return new SecuredAndCachedCodeRepositoryService(new CodeRepositoryServiceImpl(repository), operations);
    }
}
