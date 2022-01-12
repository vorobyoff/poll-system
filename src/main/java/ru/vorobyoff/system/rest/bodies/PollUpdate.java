package ru.vorobyoff.system.rest.bodies;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonCreator.Mode.PROPERTIES;

@Data
public class PollUpdate {

    @NotNull
    protected final String title;

    protected final String description;
    protected final LocalDateTime endAt;

    @JsonCreator(mode = PROPERTIES)
    PollUpdate(@JsonProperty("title") String title,
               @JsonProperty("description") String description,
               @JsonProperty("end_at") LocalDateTime endAt) {
        this.title = title;
        this.description = description;
        this.endAt = endAt;
    }
}