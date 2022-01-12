package ru.vorobyoff.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.PersistenceConstructor;

import java.time.LocalDateTime;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static java.util.Objects.nonNull;

@Data
@JsonInclude(NON_NULL)
@JsonNaming(SnakeCaseStrategy.class)
public final class Poll {

    private final Long id;
    private final String title;
    private final String description;
    private final LocalDateTime startAt;
    private final LocalDateTime endAt;
    private final Map<String, Question> questions;

    @Builder
    @PersistenceConstructor
    Poll(Long id, String title, String description, LocalDateTime startAt,
         LocalDateTime endAt, Map<String, Question> questions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startAt = startAt;
        this.endAt = endAt;
        this.questions = questions;
    }

    @JsonIgnore
    public boolean isSaved() {
        return nonNull(id);
    }

    public Poll setId(Long id) {
        return Poll.builder()
                .description(description)
                .questions(questions)
                .startAt(startAt)
                .endAt(endAt)
                .title(title)
                .id(id)
                .build();
    }
}