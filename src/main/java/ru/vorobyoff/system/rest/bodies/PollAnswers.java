package ru.vorobyoff.system.rest.bodies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;
import static java.util.Collections.unmodifiableMap;

@Data
public final class PollAnswers {

    @NotNull
    private final String userId;
    @NotEmpty
    private final Map<String, String> answers;

    @JsonCreator(mode = PROPERTIES)
    public PollAnswers(@JsonProperty("user_id") String userId,
                       @JsonProperty("answers") Map<String, String> answers) {
        this.userId = userId;
        this.answers = answers;
    }

    public Map<String, String> getAnswers() {
        return unmodifiableMap(answers);
    }
}