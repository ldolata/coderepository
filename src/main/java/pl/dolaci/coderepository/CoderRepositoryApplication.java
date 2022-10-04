package pl.dolaci.coderepository;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
@ComponentScan
@OpenAPIDefinition(info=@Info(title="Code repository crawler", version = "0.0.1"))
public class CoderRepositoryApplication {
	public static void main(String[] args) {
		SpringApplication.run(CoderRepositoryApplication.class, args);
	}

}
