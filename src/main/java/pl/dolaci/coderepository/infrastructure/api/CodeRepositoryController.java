package pl.dolaci.coderepository.infrastructure.api;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.dolaci.coderepository.domain.CodeRepositoryInfo;
import pl.dolaci.coderepository.services.CodeRepositoryService;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping ("/repositories")
@Validated
class CodeRepositoryController {

    private final CodeRepositoryService service;

    public CodeRepositoryController(CodeRepositoryService service){
        this.service = service;
    }

    @GetMapping("/{owner}/{repositoryname}")
    public Mono<CodeRepositoryInfo> getRepository(@PathVariable @NotBlank String owner, @PathVariable("repositoryname") @NotBlank String repositoryName) {
        return service.
                findRepositoryByOwnerAndRepositoryName(owner, repositoryName);
    }
}
