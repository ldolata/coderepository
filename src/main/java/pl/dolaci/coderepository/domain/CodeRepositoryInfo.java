package pl.dolaci.coderepository.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.time.LocalDateTime;


@Builder
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class CodeRepositoryInfo implements Serializable {
    String fullName;
    String description;
    String cloneUrl;
    Integer stars;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat( pattern="yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt;
}
