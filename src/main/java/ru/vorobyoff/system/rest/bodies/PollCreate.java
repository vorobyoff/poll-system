package ru.vorobyoff.system.rest.bodies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import ru.vorobyoff.system.domain.Question;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;
import static java.util.Collections.unmodifiableSet;


@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
public final class PollCreate extends PollUpdate {

    @NotNull
    private final LocalDateTime startAt;
    @NotEmpty
    private final Set<Question> questions;

    @JsonCreator(mode = PROPERTIES)
    PollCreate(@JsonProperty("start_at") LocalDateTime startAt,
               @JsonProperty("questions") Set<Question> questions,
               @JsonProperty("description") String description,
               @JsonProperty("end_at") LocalDateTime endAt,
               @JsonProperty("title") String title) {
        super(title, description, endAt);
        this.startAt = startAt;
        this.questions = questions;
    }

    public Set<Question> getQuestions() {
        return unmodifiableSet(questions);
    }
}