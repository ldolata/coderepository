package pl.dolaci.coderepository.infrastructure.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.dolaci.coderepository.repositories.CodeRepositoryRepository;
import pl.dolaci.coderepository.services.CodeRepositoryServiceImpl;

@Configuration
class ServicesConfiguration {
    @Bean
    public SecuredCodeRepositoryService codeRepositoryService(CodeRepositoryRepository repository) {
        return new SecuredCodeRepositoryService(new CodeRepositoryServiceImpl(repository));
    }
}
