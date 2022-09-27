package pl.dolaci.coderepository.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;


@Builder
@Getter
@ToString
public class CodeRepositoryInfo {
    String fullName;
    String description;
    String cloneUrl;
    Integer stars;
    LocalDateTime createdAt;
}
