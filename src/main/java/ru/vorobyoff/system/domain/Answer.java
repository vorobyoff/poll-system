package ru.vorobyoff.system.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@JsonInclude(NON_NULL)
@JsonNaming(SnakeCaseStrategy.class)
public final class Answer {

    private final Long id;
    private final Poll poll;
    private final String userId;
    private final Map<String, String> answers;

    public static Answer of(Poll poll, String userId, Map<String, String> answers) {
        return new Answer(null, poll, userId, answers);
    }

    public Answer setId(Long id) {
        return new Answer(id, poll, userId, answers);
    }
}