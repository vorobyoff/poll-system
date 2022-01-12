package ru.vorobyoff.system.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;
import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
@JsonNaming(SnakeCaseStrategy.class)
public final class Question {

    @NotNull
    @NotBlank
    private final String text;
    @NotNull
    private final Question.Type type;

    private final Set<String> answers;

    @Builder
    @JsonCreator(mode = PROPERTIES)
    Question(@JsonProperty("text") String text,
             @JsonProperty("type") Type type,
             @JsonProperty("answers") Set<String> answers) {
        this.text = text;
        this.type = type;
        this.answers = answers;
    }

    public enum Type {
        TEXT, SINGLE, MULTIPLE
    }
}